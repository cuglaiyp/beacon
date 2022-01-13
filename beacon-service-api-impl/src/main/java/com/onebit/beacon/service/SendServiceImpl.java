package com.onebit.beacon.service;

import com.onebit.beacon.domain.BatchSendRequest;
import com.onebit.beacon.domain.SendRequest;
import com.onebit.beacon.domain.SendResponse;
import com.onebit.beacon.domain.SendTaskModel;
import com.onebit.beacon.enums.RequestType;
import com.onebit.beacon.pipeline.ProcessContext;
import com.onebit.beacon.pipeline.ProcessController;
import com.onebit.beacon.pojo.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

/**
 * 服务的入口实现类。请求进来之后，肯定有很多流程需要走，所以我们把这些流程用责任链模式封装一下，放在 support 里面
 */
public class SendServiceImpl implements SendService {

    /**
     * 首先得有流水线控制器
     */
    @Autowired
    private ProcessController processController;

    @Override
    public SendResponse send(SendRequest sendRequest) {
        /**
         * 请求进来后得让流水线进行处理，流水线需要啥？上下文（ProcessContext）! 上下文除了一些常见数据还需要啥？数据（ProcessModel）
         *      1. 构建 ProcessModel
         *      2. 构建 ProcessContext
         */
        // 1. 构建 ProcessModel
        // ProcessModel 是接口，我们需要自定自己的业务的是数据类型
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .requestType(RequestType.SINGLE.getCode())
                .messageParam(sendRequest.getMessageParam())
                .taskInfo(TaskInfo.builder().messageTemplateId(sendRequest.getMessageTemplateId()).build())
                .build();
        // 2. 构建 ProcessContext
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .build();
        processController.process(context);
        return SendResponse.builder().code(context.getCode()).msg(context.getResponse().getMsg()).build();
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        // 1. 构建 ProcessModel
        // ProcessModel 是接口，我们需要自定自己的业务的是数据类型
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .requestType(RequestType.BATCH.getCode())
                .messageParamList(batchSendRequest.getMessageParamList())
                .taskInfo(TaskInfo.builder().messageTemplateId(batchSendRequest.getMessageTemplateId()).build())
                .build();
        // 2. 构建 ProcessContext
        ProcessContext context = ProcessContext.builder()
                .code(batchSendRequest.getCode())
                .processModel(sendTaskModel)
                .build();
        processController.process(context);
        return SendResponse.builder().code(context.getCode()).msg(context.getResponse().getMsg()).build();
    }
}
