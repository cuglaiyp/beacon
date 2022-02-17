package com.onebit.beacon.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 将去重类型抽取成枚举
 *
 * @Author: Onebit
 * @Date: 2022/2/16
 */

@Getter
@ToString
@AllArgsConstructor
public enum DeduplicationType {
    CONTENT(10, "N分钟相同内容去重"),
    FREQUENCY(20, "一天内N次相同渠道去重")
    ;
    private Integer code;
    private String description;

    /**
     * 获取定义好的所有去重类型的 code
     * @return
     */
    public static List<Integer> getDeduplicationList() {
        ArrayList<Integer> result = new ArrayList<>();
        for (DeduplicationType v : DeduplicationType.values()) {
            result.add(v.getCode());
        }
        return result;
    }
}
