package com.onebit.beacon.service;

import com.onebit.beacon.domain.BatchSendRequest;
import com.onebit.beacon.domain.SendRequest;
import com.onebit.beacon.domain.SendResponse;
import com.onebit.beacon.domain.SendTaskModel;
import com.onebit.beacon.pipeline.ProcessContext;
import com.onebit.beacon.pipeline.ProcessController;
import com.onebit.beacon.vo.BasicResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

/**
 * 服务的入口实现类。请求进来之后，肯定有很多流程需要走，所以我们把这些流程用责任链模式封装一下，放在 support 里面
 */
@Service
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
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Arrays.asList(sendRequest.getMessageParam()))
                .build();
        // 2. 构建 ProcessContext
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success())
                .build();
        context = processController.process(context);
        return SendResponse.builder().code(context.getCode()).msg(context.getResponse().getMsg()).build();
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        // 1. 构建 ProcessModel
        // ProcessModel 是接口，我们需要自定自己的业务的是数据类型
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(batchSendRequest.getMessageTemplateId())
                .messageParamList(batchSendRequest.getMessageParamList())
                .build();
        // 2. 构建 ProcessContext
        ProcessContext context = ProcessContext.builder()
                .code(batchSendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success())
                .build();
        context = processController.process(context);
        return SendResponse.builder().code(context.getCode()).msg(context.getResponse().getMsg()).build();
    }
}
