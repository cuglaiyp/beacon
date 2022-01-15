package com.onebit.beacon.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.onebit.beacon.dao.SmsRecordDao;
import com.onebit.beacon.domain.SmsRecord;
import com.onebit.beacon.dto.SmsContentModel;
import com.onebit.beacon.enums.ChannelType;
import com.onebit.beacon.domain.SmsParam;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.script.SmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 */
@Component
public class SmsHandler extends Handler {

    public SmsHandler(){
        this.channelCode = ChannelType.SMS.getCode();
    }

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;

    @Override
    public void handle(TaskInfo taskInfo) {
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
                // 目前只用了腾讯云，先写死这个
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道")
                .build();
        // 接着发送消息，并返回回执
        List<SmsRecord> smsRecords = smsScript.send(smsParam);
        if(CollUtil.isNotEmpty(smsRecords)) {
            // 接着调用 dao 入库
            smsRecordDao.saveAll(smsRecords);
        }
    }
}
