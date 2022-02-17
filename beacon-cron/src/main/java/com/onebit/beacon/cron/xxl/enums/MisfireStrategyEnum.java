package com.onebit.beacon.cron.xxl.enums;

/**
 * 调度过期策略
 * @Author: Onebit
 * @Date: 2022/2/17
 */
public enum MisfireStrategyEnum {
    /**
     * do nothing
     */
    DO_NOTHING,

    /**
     * fire once now
     */
    FIRE_ONCE_NOW,
    ;
}
