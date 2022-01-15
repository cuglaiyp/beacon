package com.onebit.beacon.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.onebit.beacon.constant.BeaconConstant;
import com.onebit.beacon.dao.MessageTemplateDao;
import com.onebit.beacon.domain.MessageParam;
import com.onebit.beacon.domain.MessageTemplate;
import com.onebit.beacon.domain.SendTaskModel;
import com.onebit.beacon.dto.ContentModel;
import com.onebit.beacon.enums.ChannelType;
import com.onebit.beacon.enums.RespStatusEnum;
import com.onebit.beacon.pipeline.BusinessProcess;
import com.onebit.beacon.pipeline.ProcessContext;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.util.ContentHolderUtil;
import com.onebit.beacon.util.TaskInfoUtil;
import com.onebit.beacon.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 * 流水线结点，参数拼装
 */
@Slf4j
public class AssembleAction implements BusinessProcess {
    @Autowired
    private MessageTemplateDao messageTemplateDao;
    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        try {
            // 查询模板
            Optional<MessageTemplate> messageTemplate = messageTemplateDao.findById(messageTemplateId);
            if (!messageTemplate.isPresent() || messageTemplate.get().getIsDeleted().equals(BeaconConstant.TRUE)) {
                context.setNeedBreak(true)
                        .setResponse(BasicResultVO.fail(RespStatusEnum.TEMPLATE_NOT_FOUND));
                return;
            }
            // 组装消息
            List<TaskInfo> taskInfos = assembleTaskInfo(sendTaskModel, messageTemplate.get());
            sendTaskModel.setTaskInfoList(taskInfos);
        }catch (Exception e) {
            // 组装消息有异常
            context.setNeedBreak(true)
                    .setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("assemble task info fail! template: {}, e: {}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 根据模板 + 模板数据拼接 TaskInfo
     * @param sendTaskModel
     * @param messageTemplate
     * @return
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtil.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam))
                    .deduplicationTime(messageTemplate.getDeduplicationTime())
                    .isNightShield(messageTemplate.getIsNightShield()).build();

            taskInfoList.add(taskInfo);
        }

        return taskInfoList;

    }

    /**
     * 根据模板 + 用户传进的模板数据，替换模板中的占位符，返回替换好的数据
     * @param messageTemplate
     * @param messageParam
     * @return
     */
    private static ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {
        Integer sendChannel = messageTemplate.getSendChannel();
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());
        Class contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);

        /**
         *  反射获取得到不同的渠道对应的值
         */
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = (ContentModel) ReflectUtil.newInstance(contentModelClass);
        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());

            if (StrUtil.isNotBlank(originValue)) {
                String resultValue = ContentHolderUtil.replacePlaceHolder(originValue, variables);
                ReflectUtil.setFieldValue(contentModel, field, resultValue);
            }
        }
        return contentModel;
    }
}
