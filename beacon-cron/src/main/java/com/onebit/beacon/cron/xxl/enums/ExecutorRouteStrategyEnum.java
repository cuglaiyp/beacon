package com.onebit.beacon.cron.xxl.enums;

/**
 * 路由策略
 * @Author: Onebit
 * @Date: 2022/2/17
 */
public enum ExecutorRouteStrategyEnum {
    FIRST,
    LAST,
    ROUND,
    RANDOM,
    CONSISTENT_HASH,
    LEAST_FREQUENTLY_USED,
    LEAST_RECENTLY_USED,
    FAILOVER,
    BUSYOVER,
    SHARDING_BROADCAST,
    ;
}
