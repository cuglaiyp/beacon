package com.onebit.beacon.support.pipeline;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

import com.onebit.beacon.common.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 责任链中的上下文，责任链需要的信息都存储在这里面
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ProcessContext {
    /**
     * 标识该责任链的代码
     */
    private String code;

    /**
     * 存储着具体业务数据的模型
     */
    private ProcessModel processModel;

    /**
     * 责任链中断标识
     */
    private Boolean needBreak;

    /**
     * 责任链处理后的结果。默认成功，失败了需要自己手动设置
     */
    BasicResultVO response;
}
