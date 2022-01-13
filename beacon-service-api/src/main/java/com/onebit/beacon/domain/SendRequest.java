package com.onebit.beacon.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Data
@Accessors(chain = true)
public class SendRequest {
    /**
     * 业务类型代码
     */
    private String code;
    /**
     * 消息的模板Id
     */
    private Long messageTemplateId;
    /**
     * 消息相关的参数
     */
    private MessageParam messageParam;
}
