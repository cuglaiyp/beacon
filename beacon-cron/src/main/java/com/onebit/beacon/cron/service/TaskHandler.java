package com.onebit.beacon.cron.service;

/**
 * @Author: Onebit
 * @Date: 2022/2/18
 */
public interface TaskHandler {
    void handle(Long messageTemplateId);
}
