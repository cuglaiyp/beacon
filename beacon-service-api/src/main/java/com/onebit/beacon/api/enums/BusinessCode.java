package com.onebit.beacon.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 关联流水线的 code
 *
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Getter
@ToString
@AllArgsConstructor
public enum BusinessCode {

    COMMON_SEND("send", "普通发送"),

    RECALL("recall", "撤回消息");


    /** code 关联着是哪条责任链流水线 */
    private String code;

    /** 类型说明 */
    private String description;


}
