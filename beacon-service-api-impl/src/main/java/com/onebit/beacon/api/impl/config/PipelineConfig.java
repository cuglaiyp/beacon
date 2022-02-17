package com.onebit.beacon.api.impl.config;

import com.onebit.beacon.api.impl.action.AssembleAction;
import com.onebit.beacon.api.impl.action.PostParamCheckAction;
import com.onebit.beacon.api.impl.action.PreParamCheckAction;
import com.onebit.beacon.api.impl.action.SendMqAction;
import com.onebit.beacon.api.enums.BusinessCode;
import com.onebit.beacon.support.pipeline.BusinessProcess;
import com.onebit.beacon.support.pipeline.ProcessController;
import com.onebit.beacon.support.pipeline.ProcessTemplate;
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
        processList.add(preParamCheckAction());
        processList.add(assembleAction());
        processList.add(postParamCheckAction());
        processList.add(sendMqAction());
        return processTemplate;
    }

    @Bean
    public BusinessProcess preParamCheckAction() {
        return new PreParamCheckAction();
    }

    @Bean
    public BusinessProcess assembleAction() {
        return new AssembleAction();
    }

    @Bean
    public BusinessProcess postParamCheckAction(){
        return new PostParamCheckAction();
    }

    @Bean
    public BusinessProcess sendMqAction() {
        return new SendMqAction();
    }



}
