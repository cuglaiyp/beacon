package com.onebit.beacon.api.impl.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.onebit.beacon.api.impl.domain.SendTaskModel;
import com.onebit.beacon.common.enums.RespStatusEnum;
import com.onebit.beacon.support.pipeline.BusinessProcess;
import com.onebit.beacon.support.pipeline.ProcessContext;
import com.onebit.beacon.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Slf4j
public class SendMqAction implements BusinessProcess {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${beacon.business.topic.name}")
    private String topicName;


    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        try {
            kafkaTemplate.send(topicName,
                    JSON.toJSONString(sendTaskModel.getTaskInfoList(),
                            new SerializerFeature[]{SerializerFeature.WriteClassName}));
        } catch (Exception e) {
            context.setNeedBreak(true)
                    .setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send kafka fail! e: {}", Throwables.getStackTraceAsString(e));
        }
    }
}
