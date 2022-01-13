package com.onebit.beacon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@ToString
@AllArgsConstructor
@Getter
public enum RespStatusEnum {
    SUCCESS("00000", "操作成功"),
    FAIL("00001", "操作失败"),

    /**
     * 客户端出错
     */
    CLIENT_BAD_PARAMETERS("A0100", "客户端参数错误"),

    /**
     * 服务端出错
     */
    SERVICE_ERROR("B0001", "服务执行异常"),
    RESOURCE_NOT_FOUND("B0404", "资源不存在")

    ;
    private final String code;
    private final String msg;
}
