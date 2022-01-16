package com.onebit.beacon.service.deduplication;

import cn.hutool.core.date.DateUtil;
import com.onebit.beacon.domain.DeduplicationParam;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.enums.AnchorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Service
public class DeduplicationRuleService {

    @Autowired
    private ContentDeduplicationService contentDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;

    public void deduplicate(TaskInfo taskInfo) {
        // 内容去重
        DeduplicationParam contentParam = DeduplicationParam.builder()
                .deduplicationTime(300L)
                .countNum(1)
                .taskInfo(taskInfo)
                .anchorState(AnchorState.CONTENT_DEDUPLICATION)
                .build();
        contentDeduplicationService.deduplication(contentParam);

        // 频次去重
        Long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam freParam = DeduplicationParam.builder()
                .deduplicationTime(seconds)
                .countNum(5)
                .taskInfo(taskInfo)
                .anchorState(AnchorState.RULE_DEDUPLICATION)
                .build();
        frequencyDeduplicationService.deduplication(freParam);
    }
}
