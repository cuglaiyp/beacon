package com.onebit.beacon.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 *
 * 处理与 TaskInfo 有关任务的工具类
 */
public class TaskInfoUtils {

    private static int TYPE_FLAG = 1000000;

    /**
     * 从用户的请求，生成 TaskInfo 的 BusinessId
     * 模板类型+模板ID+当天日期
     * (固定16位)
     */
    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"));
        return Long.valueOf(String.format("%d%s", templateType * TYPE_FLAG + templateId, today));
    }
}
