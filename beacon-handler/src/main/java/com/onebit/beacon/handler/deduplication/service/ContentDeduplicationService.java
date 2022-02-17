package com.onebit.beacon.handler.deduplication.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.onebit.beacon.common.domain.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService {


    /**
     * key：md5(templateId + receiver + content)
     * 相同的内容模板短时间内不能发给同一个人
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    protected String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(
                taskInfo.getMessageTemplateId()
                        + receiver
                        + JSON.toJSONString(taskInfo.getContentModel())
        );
    }
}
