package com.onebit.beacon.pending;

import com.onebit.beacon.handler.HandlerHolder;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.service.deduplication.DeduplicationRuleService;
import com.onebit.beacon.service.discard.DiscardMessageService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Data
@Accessors(chain = true)
public class Task implements Runnable {

    private TaskInfo taskInfo;

    @Autowired
    private DeduplicationRuleService deduplicationRuleService;

    @Autowired
    private DiscardMessageService discardMessageService;

    @Autowired
    private HandlerHolder handlerHolder;

    @Override
    public void run() {

        // 判断一下是否需要丢弃这条消息
        if (discardMessageService.isDiscard(taskInfo)) {
            return;
        }

        // 消息来了，还要去一下重：比如 MQ 的重复消费；或者业务方出错重复发送，会导致用户在短时间收到很多条同样的消息
        deduplicationRuleService.deduplicate(taskInfo);

        // 通过具体的 handler，将消息发送出去，比如：邮件、短信
        // 那么也就需要一个 HandlerHolder 做映射
        handlerHolder.route(taskInfo.getSendChannel())
                .doHandle(taskInfo);
    }
}
