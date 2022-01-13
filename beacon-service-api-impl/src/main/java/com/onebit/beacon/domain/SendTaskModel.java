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
    // private int requestType;
    // 并不需要这个，因为传进来的任务个数就可以区分了


    /**
     * single 接口需要请求参数
     */
    // private MessageParam messageParam;
    // 也不需要这个，同样根据个数判断即可

    /**
     * single/batch 接口需要的请求参数
     */
    private List<MessageParam> messageParamList;

    /**
     * 真正被发送的消息
     */
    private List<TaskInfo> taskInfoList;

    /**
     * 消息模板的 Id
     */
    private Long messageTemplateId;
}
