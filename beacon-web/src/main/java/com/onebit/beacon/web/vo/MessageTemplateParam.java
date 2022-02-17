package com.onebit.beacon.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageTemplateParam {
    /**
     * 当前页码
     */
    private Integer page ;

    /**
     * 当前页大小
     */
    private Integer perPage;

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 消息接收者(测试发送时使用)
     */
    private String receiver;

    /**
     * 下发参数信息
     */
    private String msgContent;

}
