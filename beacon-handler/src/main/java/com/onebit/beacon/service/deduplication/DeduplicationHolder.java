package com.onebit.beacon.service.deduplication;

import com.onebit.beacon.service.deduplication.builder.Builder;
import com.onebit.beacon.service.deduplication.service.DeduplicationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
@Service
public class DeduplicationHolder {

    private Map<Integer, Builder> builderHolder = new HashMap<>(4);
    private Map<Integer, DeduplicationService> serviceHolder = new HashMap<>(4);

    public Builder selectBuilder(Integer deduplicationType) {
        return builderHolder.get(deduplicationType);
    }

    public DeduplicationService selectService(Integer deduplicationType) {
        return serviceHolder.get(deduplicationType);
    }

    public void putBuilder(Integer deduplicationType, Builder builder) {
        builderHolder.put(deduplicationType, builder);
    }

    public void putService(Integer deduplicationType, DeduplicationService service) {
        serviceHolder.put(deduplicationType, service);
    }
}
