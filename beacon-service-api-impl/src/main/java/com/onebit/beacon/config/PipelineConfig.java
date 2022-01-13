package com.onebit.beacon.config;

import com.onebit.beacon.action.AssembleAction;
import com.onebit.beacon.action.PreParamAction;
import com.onebit.beacon.action.SendMqAction;
import com.onebit.beacon.enums.BusinessCode;
import com.onebit.beacon.pipeline.BusinessProcess;
import com.onebit.beacon.pipeline.ProcessController;
import com.onebit.beacon.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
@Configuration
public class PipelineConfig {

    /**
     * 控制器根据业务类型的不同，持有多条流水线
     * 目前只有一条普通的流水线
     * @return
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>();
        // 将业务类型定义成枚举
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }

    /**
     * 组装一条普通流水线，需要将各个流水线结点装起来
     *
     * @return
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        List<BusinessProcess> processList = new ArrayList<>();
        processTemplate.setProcessList(processList);
        processList.add(preParamAction());
        processList.add(assembleAction());
        processList.add(sendMqAction());
        return processTemplate;
    }

    @Bean
    public BusinessProcess preParamAction() {
        return new PreParamAction();
    }

    @Bean
    public BusinessProcess assembleAction() {
        return new AssembleAction();
    }

    @Bean
    public BusinessProcess sendMqAction() {
        return new SendMqAction();
    }

}
