package com.onebit.beacon.handler;

import cn.hutool.core.collection.CollUtil;
import com.onebit.beacon.pojo.SmsParam;
import com.onebit.beacon.pojo.TaskInfo;
import com.onebit.beacon.script.SmsScript;
import com.onebit.beacon.dao.SmsRecordDao;
import com.onebit.beacon.domain.SmsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 */
@Component
public class SmsHandler implements Handler {

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;

    @Override
    public boolean doHandler(TaskInfo taskInfo) {
        // SmsHandler 会具体地将 TaskInfo 拆开，构造 SmsParam
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(taskInfo.getContent())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                // 目前只用了腾讯云，先写死这个
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道")
                .build();
        // 接着发送消息，并返回回执
        List<SmsRecord> smsRecords = smsScript.send(smsParam);
        if(CollUtil.isNotEmpty(smsRecords)) {
            // 接着调用 dao 入库
            smsRecordDao.saveAll(smsRecords);
            return true;
        }
        return false;
    }
}
