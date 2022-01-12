package com.onebit.beacon.handler;

import com.onebit.beacon.pojo.TaskInfo;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 */
public interface Handler {

    // 同时也需要在 SmsParam 上面包装一层任务——任务持有发送信息的所有额外信息
    boolean doHandler(TaskInfo taskInfo);
}
