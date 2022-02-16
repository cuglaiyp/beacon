package com.onebit.beacon.vo;

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

}
