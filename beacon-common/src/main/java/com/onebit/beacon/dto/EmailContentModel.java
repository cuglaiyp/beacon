package com.onebit.beacon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailContentModel extends ContentModel {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容(可写入HTML)
     */
    private String content;
}
