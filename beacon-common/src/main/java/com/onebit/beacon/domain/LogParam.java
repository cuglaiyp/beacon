package com.onebit.beacon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 给日志框架使用的日志参数
 *
 * @Author: Onebit
 * @Date: 2022/1/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogParam {
    /**
     * 需要记录的日志
     */
    private Object object;

    /**
     * 标识日志的业务
     */
    private String bizType;

    /**
     * 生成时间
     */
    private long timestamp;
}
