package com.onebit.beacon.handler.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.onebit.beacon.common.domain.TaskInfo;
import com.onebit.beacon.common.enums.AnchorState;
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
    @JSONField(name = "time")
    private Long deduplicationTime;

    @JSONField(name = "num")
    /**
     * 需达到的次数去重
     */
    private Integer countNum;

    /**
     * 标识属于哪种去重（数据埋点）
     */
    private AnchorState anchorState;
}
