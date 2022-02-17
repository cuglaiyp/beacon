package com.onebit.beacon.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送方式（channel）-> 具体 Handler
 *
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Component
public class HandlerHolder {
    private Map<Integer, BaseHandler> handlers = new HashMap<>(32);

    public void putHandler(Integer channelCode, BaseHandler handler) {
        handlers.put(channelCode, handler);
    }

    public BaseHandler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}
