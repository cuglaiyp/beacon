package com.onebit.beacon.service.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.onebit.beacon.constant.BeaconConstant;
import com.onebit.beacon.domain.AnchorInfo;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.enums.AnchorState;
import com.onebit.beacon.util.LogUtil;
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
     *
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        // 从 apollo 中取的配置
        JSONArray jsonArray = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                BeaconConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        if (jsonArray.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            LogUtil.print(
                    AnchorInfo.builder()
                            .businessId(taskInfo.getBusinessId())
                            .state(AnchorState.DISCARD.getCode())
                            .ids(taskInfo.getReceiver())
                            .build()
            );
            return true;
        }
        return false;
    }
}
