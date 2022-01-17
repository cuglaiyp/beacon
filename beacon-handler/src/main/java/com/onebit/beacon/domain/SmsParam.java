package com.onebit.beacon.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SmsParam {
    /**
     * 模板id
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
     * 发送账号
     */
    private Integer sendAccount;
}
