package com.onebit.beacon.api.service;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

import com.onebit.beacon.api.domain.BatchSendRequest;
import com.onebit.beacon.api.domain.SendRequest;
import com.onebit.beacon.api.domain.SendResponse;

/**
 * 给外部调用的发送消息接口（一般以 rpc 服务的形式暴露给其他服务）
 */
public interface SendService {
    /**
     * 单次发送
     */
    SendResponse send(SendRequest sendRequest);

    /**
     * 批量发送
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);

    // 接口需要参数和返回值
}
