package com.onebit.beacon.service.deduplication.builder;

import cn.hutool.core.date.DateUtil;
import com.onebit.beacon.domain.DeduplicationParam;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.enums.AnchorState;
import com.onebit.beacon.enums.DeduplicationType;

import java.util.Date;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder {

    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}
