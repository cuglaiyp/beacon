package com.onebit.beacon.api.impl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

@Getter
@ToString
@AllArgsConstructor
public enum RequestType {

    SINGLE(10, "请求接口为 single 类型"),

    BATCH(20, "请求接口为 batch 类型");

    /**
     * code
     */
    private Integer code;

    /**
     * 类型说明
     */
    private String description;
}

