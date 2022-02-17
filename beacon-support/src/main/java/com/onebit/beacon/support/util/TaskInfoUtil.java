package com.onebit.beacon.support.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 * <p>
 * 处理与 TaskInfo 有关任务的工具类
 */
public class TaskInfoUtil {

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

    /**
     * 给 url 拼接平台参数，用于数据追踪
     * @param url
     * @param templateId
     * @param templateType
     * @return
     */
    public static String generateUrl(String url, Long templateId, Integer templateType) {
        url = url.trim();
        Long businessId = generateBusinessId(templateId, templateType);
        if (url.indexOf('?') == -1) {
            return url + "?track_code_bid=" + businessId;
        }
        return url + "&track_code_bid=" + businessId;
    }
}
