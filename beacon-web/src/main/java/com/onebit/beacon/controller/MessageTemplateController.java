package com.onebit.beacon.controller;

import com.onebit.beacon.dao.MessageTemplateDao;
import com.onebit.beacon.domain.MessageTemplate;
import com.onebit.beacon.vo.BasicResultVO;
import com.onebit.beacon.vo.MessageTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

@RestController
@RequestMapping("/messageTemplate")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * 插入，如果存在则修改
     */
    @PostMapping("/save")
    public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateDao.save(messageTemplate);
        return BasicResultVO.success(info);

    }

    /**
     * 查询模板
     */
    @GetMapping("/query")
    public BasicResultVO query() {
        Iterable<MessageTemplate> all = messageTemplateDao.findAll();
        long count = messageTemplateDao.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().rows(all).count(count).build();
        return BasicResultVO.success(messageTemplateVo);
    }
}
