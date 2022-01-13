package com.onebit.beacon.domain;

import com.onebit.beacon.pipeline.ProcessModel;
import com.onebit.beacon.pojo.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {
    /**
     * 请求类型：
     *      10：single
     *      20：batch
     */
    private int requestType;

    /**
     * single 接口需要的数据
     */
    private MessageParam messageParam;

    /**
     * batch 接口需要的数据
     */
    private List<MessageParam> messageParamList;

    /**
     * 真正被发送的消息
     */
    private TaskInfo taskInfo;
}
