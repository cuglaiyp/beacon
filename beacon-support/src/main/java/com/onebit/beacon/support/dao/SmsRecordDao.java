package com.onebit.beacon.support.dao;

import com.onebit.beacon.support.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {
}