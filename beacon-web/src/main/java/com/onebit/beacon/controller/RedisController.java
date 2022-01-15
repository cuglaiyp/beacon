package com.onebit.beacon.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@RestController
@Slf4j
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis")
    public void testRedis() {
        redisTemplate.opsForValue().set("1", "1");
        log.info(redisTemplate.opsForValue().get("1"));
    }
}
