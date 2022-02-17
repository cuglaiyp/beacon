package com.onebit.beacon.common.constant;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 * <p>
 * 存放一些基础的常量
 */
public class BeaconConstant {
    /**
     * 与数据库的 bool 对应
     */
    public final static Integer TRUE = 1;
    public final static Integer FALSE = 0;

    /**
     * 时间格式
     */
    public final static String YYYY_MM_DD = "yyyyMMdd";
    /**
     * cron时间格式
     */
    public final static String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";


    /**
     * apollo默认的值
     */
    public final static String APOLLO_DEFAULT_VALUE_JSON_OBJECT = "{}";
    public final static String APOLLO_DEFAULT_VALUE_JSON_ARRAY = "[]";
}
