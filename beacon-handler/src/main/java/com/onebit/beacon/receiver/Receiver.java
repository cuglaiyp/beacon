package com.onebit.beacon.receiver;

import com.alibaba.fastjson.JSON;
import com.onebit.beacon.domain.AnchorInfo;
import com.onebit.beacon.domain.LogParam;
import com.onebit.beacon.enums.AnchorState;
import com.onebit.beacon.pending.Task;
import com.onebit.beacon.pending.TaskPendingHolder;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.util.GroupIdMappingUtil;
import com.onebit.beacon.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 * 接收发送消息的请求，并发送消息
 */

@Slf4j
public class Receiver {

    private static final String LOG_BIZ_TYPE = "Receiver#consume";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    // 改造 Receiver，实现多个消费者分组，从所有消息中拿出属于自己组的消息进行消费
    @KafkaListener(topics = "#{'${beacon.topic.name}'}")
    public void consume(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfoList = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            // 判断这个 list 的 groupId 与自己的 groupId 是否相同
            String taskInfoGroupId = GroupIdMappingUtil.getGroupIdByTaskInfo(taskInfoList.get(0));
            if (taskInfoGroupId.equals(topicGroupId)) {
                for (TaskInfo taskInfo : taskInfoList) {
                    LogUtil.print(
                            LogParam.builder()
                                    .bizType(LOG_BIZ_TYPE)
                                    .object(taskInfo)
                                    .build()
                            ,
                            AnchorInfo.builder()
                                    .businessId(taskInfo.getBusinessId())
                                    .ids(taskInfo.getReceiver())
                                    .state(AnchorState.RECEIVE.getCode())
                                    .build()
                    );
                    // 1. 创建任务
                    // 在 config.PrototypeBeanConfig.class 中定义了原型模式，所以是线程安全的
                    Task task = applicationContext.getBean(Task.class).setTaskInfo(taskInfo);
                    // 2. 将任务扔进自己组的线程池，等待处理
                    // 需定义一个线程池工具类，能为不同 group 创建线程池，并且索引到对应线程池
                    taskPendingHolder.route(topicGroupId).execute(task);
                }
            }
        }
    }
}
