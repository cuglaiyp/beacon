package com.onebit.beacon.api.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Data
@Accessors(chain = true)
@Builder
public class SendRequest {
    /**
     * 业务类型代码
     */
    private String code; // 会封装到 ProcessContext 中
    /**
     * 消息的模板Id
     */
    private Long messageTemplateId; // 会封装到 SendTaskModel 中
    /**
     * 消息相关的参数
     */
    private MessageParam messageParam; // 会封装到 SendTaskModel 中
}
