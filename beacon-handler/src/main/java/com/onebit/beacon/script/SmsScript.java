package com.onebit.beacon.script;

import com.onebit.beacon.domain.SmsParam;
import com.onebit.beacon.domain.SmsRecord;
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
