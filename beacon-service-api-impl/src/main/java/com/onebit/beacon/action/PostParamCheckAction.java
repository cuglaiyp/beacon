package com.onebit.beacon.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.onebit.beacon.domain.SendTaskModel;
import com.onebit.beacon.enums.ChannelType;
import com.onebit.beacon.enums.IdType;
import com.onebit.beacon.enums.RespStatusEnum;
import com.onebit.beacon.pipeline.BusinessProcess;
import com.onebit.beacon.pipeline.ProcessContext;
import com.onebit.beacon.pojo.TaskInfo;
import com.onebit.beacon.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Slf4j
public class PostParamCheckAction implements BusinessProcess {

    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        List<TaskInfo> taskInfoList = sendTaskModel.getTaskInfoList();
        // 过滤掉不合法的手机号
        filterIllegalPhoneNum(taskInfoList);

        if (CollUtil.isEmpty(taskInfoList)) {
            context.setNeedBreak(true)
                    .setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }
    }

    private void filterIllegalPhoneNum(List<TaskInfo> taskInfoList) {
        Integer idType = taskInfoList.get(0).getIdType();
        Integer sendChannel = taskInfoList.get(0).getSendChannel();

        if (IdType.PHONE.getCode().equals(idType) && ChannelType.SMS.getCode().equals(sendChannel)) {
            Iterator<TaskInfo> iterator = taskInfoList.iterator();
            while (iterator.hasNext()) {
                TaskInfo taskInfo = iterator.next();
                List<String> illegalPhones = taskInfo.getReceiver().stream()
                        .filter(phone -> !ReUtil.isMatch(PHONE_REGEX_EXP, phone))
                        .collect(Collectors.toList());
                if (CollUtil.isNotEmpty(illegalPhones)){
                    taskInfo.getReceiver().removeAll(illegalPhones);
                    log.error("{} find illegal phone! {}", taskInfo.getMessageTemplateId(), JSON.toJSON(illegalPhones));
                }
                if (CollUtil.isEmpty(taskInfo.getReceiver())) {
                    iterator.remove();
                }
            }
        }
    }
}
