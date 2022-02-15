package com.onebit.beacon.service.deduplication.service;

import com.onebit.beacon.domain.DeduplicationParam;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public interface DeduplicationService {
    void deduplication(DeduplicationParam param);
}
