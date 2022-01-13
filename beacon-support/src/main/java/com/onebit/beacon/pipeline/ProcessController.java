package com.onebit.beacon.pipeline;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.onebit.beacon.enums.RespStatusEnum;
import com.onebit.beacon.vo.BasicResultVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 责任链流水线的入口，流程控制器
 */

@Slf4j
@Data
public class ProcessController {
    /**
     * 按业务分类保存流水线链条
     */
    private Map<String, ProcessTemplate> templateConfig;

    /**
     * 使用上下文执行每个结点
     *
     * @param context
     * @return
     */
    public ProcessContext process(ProcessContext context) {
        // 校验一下 context
        if (context == null) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.CONTEXT_IS_NULL));
            return context;
        }
        // 校验一下业务代码是否为空
        String businessCode = context.getCode();
        if (StrUtil.isBlank(businessCode)) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.BUSINESS_CODE_IS_NULL));
            return context;
        }
        // 判断业务代码对应的流水线是否为空
        ProcessTemplate processTemplate = templateConfig.get(businessCode);
        if (processTemplate == null) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.PROCESS_TEMPLATE_IS_NULL));
            return context;
        }
        List<BusinessProcess> processList = processTemplate.getProcessList();
        if (CollUtil.isEmpty(processList)) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.PROCESS_LIST_IS_NULL));
            return context;
        }
        // 开始执行流水线
        for (BusinessProcess businessProcess : processList) {
            businessProcess.process(context);
            if (context.getNeedBreak()) {
                break;
            }
        }
        return context;
    }
}
