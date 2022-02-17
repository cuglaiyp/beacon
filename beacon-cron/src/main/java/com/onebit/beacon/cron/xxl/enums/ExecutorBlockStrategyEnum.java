package com.onebit.beacon.cron.xxl.enums;

/**
 * 队列阻塞策略
 * @Author: Onebit
 * @Date: 2022/2/17
 */
public enum ExecutorBlockStrategyEnum {
    /**
     * 单机串行
     */
    SERIAL_EXECUTION,

    /**
     * 丢弃后续调度
     */
    DISCARD_LATER,

    /**
     * 覆盖之前调度
     */
    COVER_EARLY;
}
