package com.onebit.beacon.controller;

import com.onebit.beacon.domain.MessageParam;
import com.onebit.beacon.domain.SendRequest;
import com.onebit.beacon.domain.SendResponse;
import com.onebit.beacon.enums.BusinessCode;
import com.onebit.beacon.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *  发送短信的业务由 web 请求而来，web 请求封装成 common 中的 pojo，并交由 handler 处理
 *  handler 需要与 pojo 交互，并且需要 support 进行支持
 */

@RestController
public class SendController {
    @Autowired
    private SendService sendService;

    @GetMapping("/sendSms")
    public SendResponse sendSms(String phone) {
        Map<String, String> variables = new HashMap<>();
        variables.put("contentValue", "61520");
        MessageParam messageParam = new MessageParam()
                .setReceiver(phone)
                .setVariables(variables);
        SendRequest sendRequest = new SendRequest()
                .setCode(BusinessCode.COMMON_SEND.getCode())
                .setMessageTemplateId(1l)
                .setMessageParam(messageParam);
        return sendService.send(sendRequest);
    }
}
