package com.onebit.beacon.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SmsParam {
    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 发送消息需要手机号
     */
    private Set<String> phones;

    /**
     * 发送的内容，也就是替换模板占位符的地方
     */
    private String content;

    /**
     * 渠道商Id
     */
    private Integer supplierId;

    /**
     * 渠道商名字，也就是用谁发（腾讯云、阿里云等等）
     */
    private String supplierName;
}
