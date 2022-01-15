package com.onebit.beacon.util;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public Map<String, String> mGet(List<String> keys) {
        Map<String, String> result = new HashMap<>(keys.size());
        try {
            // 根据每个 key，获取对应的 value
            List<String> values = redisTemplate.opsForValue().multiGet(keys);
            if (CollUtil.isNotEmpty(values)) {
                for (int i = 0; i < keys.size(); i++) {
                    result.put(keys.get(i), values.get(i));
                }
            }
        } catch (Exception e) {
            log.error("redis mGet fail! e: {}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    /**
     * 设置每对 key-value，和其超时时间
     *
     * @param keyValues
     * @param timeout
     */
    public void pipelineSetEX(Map<String, String> keyValues, Long timeout) {
        try {
            // 这个命令可以在一个连接中执行多条命令，防止每条命令都建立连接
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    connection.setEx(entry.getKey().getBytes(), timeout, entry.getValue().getBytes());
                }
                return null;
            });
        } catch (Exception e) {
            log.error("redis pipelineSetEX fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
