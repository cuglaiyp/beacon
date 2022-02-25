package com.onebit.beacon.cron.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.onebit.beacon.api.domain.BatchSendRequest;
import com.onebit.beacon.api.domain.MessageParam;
import com.onebit.beacon.api.enums.BusinessCode;
import com.onebit.beacon.api.service.SendService;
import com.onebit.beacon.cron.constant.PendingConstant;
import com.onebit.beacon.cron.vo.CrowdInfoVo;
import com.onebit.beacon.support.pending.AbstractLazyPending;
import com.onebit.beacon.support.pending.PendingParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Onebit
 * @Date: 2022/2/18
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdInfoVo> {

    @Resource
    private SendService sendService;

    /**
     * 必须初始化父类的 PendingParam
     */
    public CrowdBatchTaskPending() {
        this.pendingParam = new PendingParam<>();
        this.pendingParam.setNumThreshold(PendingConstant.NUM_THRESHOLD)
                .setQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setTimeThreshold(PendingConstant.TIME_THRESHOLD)
                .setExecutorService(ExecutorBuilder.create()
                        .setCorePoolSize(PendingConstant.CORE_POOL_SIZE)
                        .setMaxPoolSize(PendingConstant.MAX_POOL_SIZE)
                        .setWorkQueue(PendingConstant.BLOCKING_QUEUE)
                        .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                        .build());
    }

    @Override
    public void doHandle(List<CrowdInfoVo> crowdInfoVos) {
        // 1. 如果参数相同，组装成同一个 MessageParam 发送，接收者用逗号隔开
        Map<Map<String, String>, String> paramMap = MapUtil.newHashMap();
        for (CrowdInfoVo crowdInfoVo : crowdInfoVos) {
            String receiver = crowdInfoVo.getReceiver();
            Map<String, String> vars = crowdInfoVo.getParams();
            if (paramMap.get(vars) == null) {
                paramMap.put(vars, receiver);
            } else {
                String newReceiver = StringUtils.join(new String[]{
                        paramMap.get(vars), receiver}, StrUtil.COMMA);
                paramMap.put(vars, newReceiver);
            }
        }

        // 2. 组装参数
        List<MessageParam> messageParams = Lists.newArrayList();
        for (Map.Entry<Map<String, String>, String> entry : paramMap.entrySet()) {
            MessageParam messageParam = MessageParam.builder()
                    .receiver(entry.getValue())
                    .variables(entry.getKey()).build();
            messageParams.add(messageParam);
        }

        // 3. 调用批量发送接口发送消息
        BatchSendRequest batchSendRequest = BatchSendRequest.builder()
                .code(BusinessCode.COMMON_SEND.getCode())
                .messageParamList(messageParams)
                .messageTemplateId(CollUtil.getFirst(crowdInfoVos.iterator()).getMessageTemplateId())
                .build();
        sendService.batchSend(batchSendRequest);
    }
}
