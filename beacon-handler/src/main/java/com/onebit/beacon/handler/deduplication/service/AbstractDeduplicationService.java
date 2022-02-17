package com.onebit.beacon.handler.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import com.onebit.beacon.common.constant.BeaconConstant;
import com.onebit.beacon.common.domain.AnchorInfo;
import com.onebit.beacon.handler.domain.DeduplicationParam;
import com.onebit.beacon.common.domain.TaskInfo;
import com.onebit.beacon.handler.deduplication.DeduplicationHolder;
import com.onebit.beacon.support.util.LogUtil;
import com.onebit.beacon.support.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 模板类，将去重通用逻辑封装，具体逻辑有子类实现
 *
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Slf4j
public abstract class AbstractDeduplicationService implements DeduplicationService {

    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    private void init() {
        // 存入 Holder
        deduplicationHolder.putService(deduplicationType, this);
    }

    @Autowired
    private RedisUtil redisUtil;

    public void deduplication(DeduplicationParam param) {
        TaskInfo taskInfo = param.getTaskInfo();
        // 存放需要去掉的用户
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        // 获取所有的去重 key
        List<String> keys = deduplicationAllKeys(taskInfo);
        // 判断这些 key 是否存储在 redis 中，并统计次数 [key -> count]
        Map<String, String> inRedisValue = redisUtil.mGet(keys);

        Set<String> readyPutRedisReceivers = new HashSet<>(taskInfo.getReceiver().size());

        for (String receiver : taskInfo.getReceiver()) {
            String key = deduplicationSingleKey(taskInfo, receiver);
            String value = inRedisValue.get(key);

            // 需要过滤掉的用户
            if (value != null && Integer.valueOf(value) >= param.getCountNum()) {
                filterReceiver.add(receiver);
            } else { // 不需要过滤的用户，放入 redis
                readyPutRedisReceivers.add(receiver);
            }
        }

        // 不符合条件的用户：需要更新 Redis (无记录添加，有记录则累加次数)
        putInRedis(readyPutRedisReceivers, inRedisValue, param);

        // 剔除需要去重的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            LogUtil.print(
                    AnchorInfo.builder()
                            .businessId(taskInfo.getBusinessId())
                            .ids(filterReceiver)
                            .state(param.getAnchorState().getCode())
                            .build()
            );
        }
    }

    /**
     * 获取当前消息模板所有的去重 key
     *
     * @param taskInfo
     * @return
     */
    private List<String> deduplicationAllKeys(TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());
        for (String receiver : taskInfo.getReceiver()) {
            String key = deduplicationSingleKey(taskInfo, receiver);
            result.add(key);
        }
        return result;
    }

    /**
     * 获取单个去重 key
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    protected abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);

    /**
     * 存入 redis 实现去重
     *
     * @param readyPutRedisReceivers
     * @param inRedisValue
     * @param param
     */
    private void putInRedis(Set<String> readyPutRedisReceivers,
                            Map<String, String> inRedisValue, DeduplicationParam param) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceivers.size());
        for (String receiver : readyPutRedisReceivers) {
            String key = deduplicationSingleKey(param.getTaskInfo(), receiver);
            // 本身就在 redis 里，次数 + 1
            if (inRedisValue.get(key) != null) {
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisValue.get(key) + 1)));
            } else {
                // 不在，置 1
                keyValues.put(key, String.valueOf(BeaconConstant.TRUE));
            }
        }
        if (CollUtil.isNotEmpty(keyValues)) {
            // 存入 redis 并重置超时时间
            redisUtil.pipelineSetEX(keyValues, param.getDeduplicationTime());
        }

    }

}
