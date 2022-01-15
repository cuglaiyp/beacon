package com.onebit.beacon.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Data
@Builder
public class DeduplicationParam {
    /**
     * TaskIno信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间
     * 单位：秒
     */
    private Long deduplicationTime;

    /**
     * 需达到的次数去重
     */
    private Integer countNum;
}
