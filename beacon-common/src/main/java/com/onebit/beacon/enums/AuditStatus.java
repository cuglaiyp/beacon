package com.onebit.beacon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 模板状态枚举
 *
 * @Author: Onebit
 * @Date: 2022/2/16
 */

@Getter
@ToString
@AllArgsConstructor
public enum AuditStatus {
    /**
     * 10.待审核 20.审核成功 30.被拒绝
     */
    WAIT_AUDIT(10, "待审核"),
    AUDIT_SUCCESS(20, "审核成功"),
    AUDIT_REJECT(30, "被拒绝"),
    ;
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String description;
}
