package com.onebit.beacon.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 埋点信息
 *
 * @Author: Onebit
 * @Date: 2022/1/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnchorInfo {
    /**
     * 发送给的用户
     */
    private Set<String> ids;

    /**
     * 具体的点位
     */
    private int state;

    /**
     * 业务 id，用于追踪数据的使用
     */
    private Long businessId;

    /**
     * 生成时间
     */
    private long timestamp;

}
