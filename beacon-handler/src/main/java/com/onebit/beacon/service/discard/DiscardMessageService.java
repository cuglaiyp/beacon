package com.onebit.beacon.service.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.onebit.beacon.constant.BeaconConstant;
import com.onebit.beacon.domain.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * 丢弃消息的 service
 *
 * @Author: Onebit
 * @Date: 2022/1/15
 */
@Service
public class DiscardMessageService {
    private static final String DISCARD_MESSAGE_KEY = "discard";

    @ApolloConfig("onebit.beacon")
    private Config config;

    /**
     * 是否丢弃该条消息
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        JSONArray jsonArray = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                BeaconConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        if (jsonArray.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            return true;
        }
        return false;
    }
}
