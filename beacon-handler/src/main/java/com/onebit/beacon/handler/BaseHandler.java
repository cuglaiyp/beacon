package com.onebit.beacon.handler;

import com.onebit.beacon.domain.AnchorInfo;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.enums.AnchorState;
import com.onebit.beacon.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 */
public abstract class BaseHandler implements Handler {

    /**
     * 标识渠道的 code
     * 子类生成时指定
     */
    protected Integer channelCode;

    @Autowired
    private HandlerHolder handlerHolder;

    // 子类生成时指定 channelCode，然后执行 init 方法，将自己放入 handlerHolder
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    // 同时也需要在 SmsParam 上面包装一层任务——任务持有发送信息的所有额外信息
    public void doHandle(TaskInfo taskInfo) {
        if (!handle(taskInfo)) {
            LogUtil.print(
                    AnchorInfo.builder()
                            .businessId(taskInfo.getBusinessId())
                            .state(AnchorState.SEND_FAIL.getCode())
                            .ids(taskInfo.getReceiver())
                            .build()
            );
        }
        LogUtil.print(
                AnchorInfo.builder()
                        .businessId(taskInfo.getBusinessId())
                        .state(AnchorState.SEND_SUCCESS.getCode())
                        .ids(taskInfo.getReceiver())
                        .build()
        );
    }

    public abstract boolean handle(TaskInfo taskInfo);
}
