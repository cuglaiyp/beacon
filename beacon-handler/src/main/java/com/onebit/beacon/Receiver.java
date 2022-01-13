package com.onebit.beacon;

import com.alibaba.fastjson.JSON;
import com.onebit.beacon.handler.SmsHandler;
import com.onebit.beacon.pojo.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 * 接收发送消息的请求，并发送消息
 */
@Component
@Slf4j
public class Receiver {
    @Autowired
    private SmsHandler smsHandler;

    @KafkaListener(topics = "#{'${beacon.topic.name}'}", groupId = "beacon")
    public void consumer(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> lists = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            for (TaskInfo taskInfo : lists) {
                smsHandler.doHandler(taskInfo);
            }
            log.info("receiver message:{}", JSON.toJSONString(lists));
        }

    }
}
