package com.onebit.beacon.handler.deduplication.service;

import com.onebit.beacon.handler.domain.DeduplicationParam;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public interface DeduplicationService {
    void deduplication(DeduplicationParam param);
}
