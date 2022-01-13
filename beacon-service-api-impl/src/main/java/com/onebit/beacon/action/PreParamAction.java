package com.onebit.beacon.action;

import cn.hutool.core.collection.CollUtil;
import com.onebit.beacon.domain.MessageParam;
import com.onebit.beacon.domain.SendTaskModel;
import com.onebit.beacon.enums.RespStatusEnum;
import com.onebit.beacon.pipeline.BusinessProcess;
import com.onebit.beacon.pipeline.ProcessContext;
import com.onebit.beacon.vo.BasicResultVO;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 * 责任链流水线的结点，进行前置参数检查
 */

public class PreParamAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        // 拿到上下文中的数据
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context
                    .setNeedBreak(true)
                    .setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
        }
    }
}
