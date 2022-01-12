package com.onebit.beacon.dao;

import com.onebit.beacon.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {
}