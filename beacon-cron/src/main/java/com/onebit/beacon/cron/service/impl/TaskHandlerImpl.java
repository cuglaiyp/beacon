package com.onebit.beacon.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.onebit.beacon.cron.pending.CrowdBatchTaskPending;
import com.onebit.beacon.cron.service.TaskHandler;
import com.onebit.beacon.cron.util.ReadFileUtil;
import com.onebit.beacon.cron.vo.CrowdInfoVo;
import com.onebit.beacon.support.dao.MessageTemplateDao;
import com.onebit.beacon.support.domain.MessageTemplate;
import com.onebit.beacon.support.pending.AbstractLazyPending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Author: Onebit
 * @Date: 2022/2/18
 */
@Slf4j
@Service
public class TaskHandlerImpl implements TaskHandler {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Resource
    private ApplicationContext context;

    @Async
    @Override
    public void handle(Long messageTemplateId) {
        log.info("TaskHandler handle:{}", Thread.currentThread().getName());
        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);
            return;
        }
        AbstractLazyPending<CrowdInfoVo> crowdBatchTaskPending = context.getBean(CrowdBatchTaskPending.class);
        ReadFileUtil.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtil.RECEIVER_KEY))) {
                return;
            }
            HashMap<String, String> params = ReadFileUtil.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder()
                    .messageTemplateId(messageTemplateId)
                    .params(params)
                    .receiver(row.getFieldMap().get(ReadFileUtil.RECEIVER_KEY))
                    .build();
            crowdBatchTaskPending.pending(crowdInfoVo);
        });
    }
}
