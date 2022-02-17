package com.onebit.beacon.cron.xxl.enums;

/**
 * 调度类型枚举
 *
 * @Author: Onebit
 * @Date: 2022/2/17
 */
public enum ScheduleTypeEnum {
    /**
     * 无
     */
    NONE,
    /**
     * 定时调度
     */
    CRON,

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE,
    ;

}
