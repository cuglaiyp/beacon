package com.onebit.beacon.support.pipeline;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

import lombok.Data;

import java.util.List;

/**
 * 保存着某个业务的责任链
 */
@Data
public class ProcessTemplate {
    /**
     * 链条，BusinessProcess 代表其中的一个结点
     */
    private List<BusinessProcess> processList;
}
