package com.onebit.beacon.service.deduplication;

import cn.hutool.core.util.StrUtil;
import com.onebit.beacon.domain.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * 频次去重服务
 *
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {

    private static final String PREFIX = "FRE";

    /**
     * 构建 key：receiver + templateId + sendChannel
     * 一个用户一天之内只能收到某个渠道的消息 N 次
     */
    @Override
    protected String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
