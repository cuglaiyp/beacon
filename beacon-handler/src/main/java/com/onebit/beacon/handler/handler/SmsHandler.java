package com.onebit.beacon.handler.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.onebit.beacon.support.dao.SmsRecordDao;
import com.onebit.beacon.handler.domain.SmsParam;
import com.onebit.beacon.support.domain.SmsRecord;
import com.onebit.beacon.common.domain.TaskInfo;
import com.onebit.beacon.common.dto.SmsContentModel;
import com.onebit.beacon.common.enums.ChannelType;
import com.onebit.beacon.handler.script.SmsScript;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler {

    public SmsHandler() {
        this.channelCode = ChannelType.SMS.getCode();
    }

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;

    @Override
    public boolean handle(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        String trueContent = smsContentModel.getContent();
        if (!StrUtil.isBlank(smsContentModel.getUrl())) {
            trueContent = trueContent + " " + smsContentModel.getUrl();
        }
        // SmsHandler 会具体地将 TaskInfo 拆开，构造 SmsParam
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .content(trueContent)
                .sendAccount(taskInfo.getSendAccount())
                .build();
        // 接着发送消息，并返回回执
        try {
            List<SmsRecord> smsRecords = smsScript.send(smsParam);
            if (CollUtil.isNotEmpty(smsRecords)) {
                // 接着调用 dao 入库
                smsRecordDao.saveAll(smsRecords);
            }
            return true;
        } catch (TencentCloudSDKException e) {
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return false;
        }
    }
}
