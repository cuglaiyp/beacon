package com.onebit.beacon.handler;

import com.onebit.beacon.domain.TaskInfo;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public interface Handler {
    /**
     * 发送消息处理器
     * @param taskInfo
     */
    void doHandle(TaskInfo taskInfo);
}
