package com.onebit.beacon.handler.script;

import com.onebit.beacon.handler.domain.SmsParam;
import com.onebit.beacon.support.domain.SmsRecord;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
public interface SmsScript {
    /**
     * @param smsParam 发送短信参数
     * @return 渠道商接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam) throws TencentCloudSDKException;
}
