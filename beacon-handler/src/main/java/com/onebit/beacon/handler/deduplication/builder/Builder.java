package com.onebit.beacon.handler.deduplication.builder;

import com.onebit.beacon.handler.domain.DeduplicationParam;
import com.onebit.beacon.common.domain.TaskInfo;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public interface Builder {
    String CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     * @param deduplication
     * @param taskInfo
     * @return
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}
