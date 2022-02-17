package com.onebit.beacon.handler.deduplication.builder;

import cn.hutool.core.date.DateUtil;
import com.onebit.beacon.handler.domain.DeduplicationParam;
import com.onebit.beacon.common.domain.TaskInfo;
import com.onebit.beacon.common.enums.AnchorState;
import com.onebit.beacon.common.enums.DeduplicationType;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
@Service
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
