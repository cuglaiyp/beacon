package com.onebit.beacon.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
    private Iterable<Map<String, Object>> rows;

    /**
     * rows 的数量
     */
    private Long count;
}
