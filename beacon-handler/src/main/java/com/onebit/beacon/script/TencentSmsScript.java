package com.onebit.beacon.script;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.onebit.beacon.enums.SmsStatus;
import com.onebit.beacon.pojo.SmsParam;
import com.onebit.beacon.domain.SmsRecord;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 * <p>
 * 1. 发送短信接入文档：https://cloud.tencent.com/document/api/382/55981
 * 2. 推荐直接使用SDK调用
 * 3. 推荐使用API Explorer 生成代码
 */

@Service
@Slf4j
public class TencentSmsScript implements SmsScript {

    private static final Integer PHONE_NUM = 11;
    /**
     * api相关
     */
    private static final String URL = "sms.tencentcloudapi.com";
    private static final String REGION = "ap-guangzhou";

    /**
     * 账号相关
     */
    @Value("${tencent.sms.account.secret-id}")
    private String SECRET_ID;

    @Value("${tencent.sms.account.secret-key}")
    private String SECRET_KEY;

    @Value("${tencent.sms.account.sms-sdk-app-id}")
    private String SMS_SDK_APP_ID;

    @Value("${tencent.sms.account.template-id}")
    private String TEMPLATE_ID;

    @Value("${tencent.sms.account.sign_name}")
    private String SIGN_NAME;

    public List<SmsRecord> send(SmsParam smsParam) {
        try {

            /**
             * 初始化 client
             */
            // 包装成方法
            SmsClient client = init();
            /**
             * 组装发送短信参数
             */
            // 包装成方法
            SendSmsRequest req = assembleReq(smsParam);


            /**
             * 请求，返回结果
             */
            SendSmsResponse resp = client.SendSms(req);
            // 返回结果也需要处理 SmsRecord，需要返回集合
            List<SmsRecord> smsRecords = assembleSmsRecord(smsParam, resp);
            return smsRecords;


        } catch (TencentCloudSDKException e) {
            log.error("send tencent sms fail!{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }
    }

    private SmsClient init() {
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(URL);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, REGION, clientProfile);
    }

    private SendSmsRequest assembleReq(SmsParam smsParam) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(SMS_SDK_APP_ID);
        req.setSignName(SIGN_NAME);
        req.setTemplateId(TEMPLATE_ID);
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, SendSmsResponse response) {
        if (response == null || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return null;
        }
        List<SmsRecord> smsRecordList = new ArrayList<>();

        for (SendStatus sendStatus : response.getSendStatusSet()) {
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber()) // 内层是去掉 +86
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();

            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd")))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(smsParam.getSupplierId())
                    .supplierName(smsParam.getSupplierName())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .reportContent(sendStatus.getCode())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();
            smsRecordList.add(smsRecord);
        }
        return smsRecordList;
    }

}
