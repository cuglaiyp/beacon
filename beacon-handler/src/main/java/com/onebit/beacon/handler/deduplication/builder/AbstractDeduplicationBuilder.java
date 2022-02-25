package com.onebit.beacon.handler.deduplication.builder;

import com.alibaba.fastjson.JSONObject;
import com.onebit.beacon.handler.domain.DeduplicationParam;
import com.onebit.beacon.common.domain.TaskInfo;
import com.onebit.beacon.handler.deduplication.DeduplicationHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public abstract class AbstractDeduplicationBuilder implements Builder {

    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    public void init() {
        deduplicationHolder.putBuilder(deduplicationType, this);
    }

    /**
     * 根据子类从配置中心拉取的去重配置构建去重参数
     *
     * @param key
     * @param duplicationConfig
     * @param taskInfo
     * @return
     */
    public DeduplicationParam getParamsFromConfig(Integer key, String duplicationConfig, TaskInfo taskInfo) {
        JSONObject jsonObject = JSONObject.parseObject(duplicationConfig);
        if (jsonObject == null) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(jsonObject.getString(CONFIG_PRE + key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setTaskInfo(taskInfo);
        return deduplicationParam;
    }
}
