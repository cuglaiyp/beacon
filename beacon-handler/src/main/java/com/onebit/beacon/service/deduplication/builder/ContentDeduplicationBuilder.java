package com.onebit.beacon.service.deduplication.builder;

import com.onebit.beacon.domain.DeduplicationParam;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.enums.AnchorState;
import com.onebit.beacon.enums.DeduplicationType;
import org.springframework.stereotype.Service;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder {


    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;
    }
}
