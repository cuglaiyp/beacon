package com.onebit.beacon.vo;

import com.onebit.beacon.domain.MessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {
    /**
     * 消息模板列表
     */
    private Iterable<MessageTemplate> rows;

    /**
     * rows 的数量
     */
    private Long count;
}
