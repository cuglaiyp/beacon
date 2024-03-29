package com.onebit.beacon.api.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Data
@Builder
@Accessors(chain = true)
public class BatchSendRequest {
    /**
     * 业务类型代码
     */
    private String code;
    /**
     * 消息的模板Id
     */
    private Long messageTemplateId;
    /**
     * 消息相关的参数
     */
    List<MessageParam> messageParamList;
}
