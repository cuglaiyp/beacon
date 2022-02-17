package com.onebit.beacon.api.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Data
@Builder
public class SendResponse {
    /**
     * 响应状态
     */
    private String code;
    /**
     * 响应消息
     */
    private String msg;
}
