package com.onebit.beacon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author: Onebit
 * @Date: 2022/1/16
 */
@Getter
@AllArgsConstructor
@ToString
public enum TemplateType {
    OPERATION(10, "运营类的模板"),
    TECHNOLOGY(20, "技术类的模板"),
    ;

    private Integer code;
    private String description;
}
