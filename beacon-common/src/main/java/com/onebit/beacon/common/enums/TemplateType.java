package com.onebit.beacon.common.enums;

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
    /**
     * 10.定时类的模板(后台定时调用) 20.实时类的模板(接口实时调用)
     */
    CLOCKING(10, "定时类的模板(后台定时调用)"),
    REALTIME(20, "实时类的模板(接口实时调用)"),
    ;
    private Integer code;
    private String description;
}
