package com.onebit.beacon.support.dao;

import com.onebit.beacon.support.domain.MessageTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
public interface MessageTemplateDao extends JpaRepository<MessageTemplate, Long> {
    /**
     * 根据是否删除分页查询
     *
     * @param deleted  0：未删，1：删除
     * @param pageable 分页对象
     * @return
     */
    List<MessageTemplate> findAllByIsDeletedEquals(Integer deleted, Pageable pageable);

    /**
     * 统计未删除的条目
     *
     * @param deleted
     * @return
     */
    Long countByIsDeletedEquals(Integer deleted);
}

