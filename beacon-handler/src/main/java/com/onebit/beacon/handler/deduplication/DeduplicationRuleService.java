package com.onebit.beacon.handler.deduplication;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.onebit.beacon.common.constant.BeaconConstant;
import com.onebit.beacon.handler.domain.DeduplicationParam;
import com.onebit.beacon.common.domain.TaskInfo;
import com.onebit.beacon.common.enums.DeduplicationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Service
public class DeduplicationRuleService {

    public static final String DEDUPLICATION_RULE_KEY = "deduplication";

    @ApolloConfig("onebit.beacon")
    private Config config;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    public void deduplicate(TaskInfo taskInfo) {
        // 获取配置：配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, BeaconConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);

        // 去重
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();
        for (Integer deduplicationType : deduplicationList) {
            DeduplicationParam deduplicationParam = deduplicationHolder.selectBuilder(deduplicationType)
                    .build(deduplicationConfig, taskInfo);
            if (deduplicationParam != null) {
                deduplicationHolder.selectService(deduplicationType)
                        .deduplication(deduplicationParam);
            }
        }
    }
}
