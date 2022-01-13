package com.onebit.beacon.controller;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

import com.onebit.beacon.kafkatest.UserLogProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用来测试 kafka 是否配置成功
 */
@RestController
public class KafkaTestController {

    @Autowired
    private UserLogProducer userLogProducer;

    @GetMapping("/kafka/produce")
    public void produce(String userId){
        userLogProducer.sendLog(userId);
    }
}
