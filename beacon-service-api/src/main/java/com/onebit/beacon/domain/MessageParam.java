package com.onebit.beacon.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Data
@Accessors(chain = true)
public class MessageParam {
    /**
     * 消息的接收者，也就是一串电话号码
     */
    private String receiver;

    /**
     * 消息内容中一些可变的部分
     */
    private Map<String, String> variables;

    /**
     * 扩展的参数
     */
    private Map<String, String> extra;
}
