package com.onebit.beacon.controller;

import com.onebit.beacon.domain.SendRequest;
import com.onebit.beacon.domain.SendResponse;
import com.onebit.beacon.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *  发送短信的业务由 web 请求而来，web 请求封装成 common 中的 pojo，并交由 handler 处理
 *  handler 需要与 pojo 交互，并且需要 support 进行支持
 */

@RestController
public class SendController {
    @Autowired
    private SendService sendService;

    /**
     * 发送消息接口
     * 示例：curl -XPOST "127.0.0.1:8080/send"  -H "Content-Type: application/json"  -d "{\"code\":\"send\",\"messageParam\":{\"receiver\":\"1585962327@qq.com\",\"variables\":{\"title\":\"yyyyyy\",\"contentValue\":\"6666164180\"}},\"messageTemplateId\":2}"
     * @return
     */
    @PostMapping("/send")
    public SendResponse send(@RequestBody SendRequest sendRequest) {
        return sendService.send(sendRequest);
    }
}
