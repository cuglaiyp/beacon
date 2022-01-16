package com.onebit.beacon.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.onebit.beacon.dao.MessageTemplateDao;
import com.onebit.beacon.domain.MessageTemplate;
import com.onebit.beacon.enums.ChannelType;
import com.onebit.beacon.enums.IdType;
import com.onebit.beacon.enums.MessageType;
import com.onebit.beacon.enums.TemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

@RestController
public class MessageTemplateController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * test insert
     */
    @GetMapping("/insert")
    public String insert() {

        MessageTemplate messageTemplate = MessageTemplate.builder()
                .name("test短信")
                .auditStatus(10)
                .flowId("yyyy")
                .messageStatus(10)
                .idType(IdType.USER_ID.getCode())
                .sendChannel(ChannelType.IM.getCode())
                .templateType(TemplateType.TECHNOLOGY.getCode())
                .messageType(MessageType.AUTH_CODE.getCode())
                .expectPushTime("0")
                .messageContent("{\"content\":\"{$contentValue}\"}")
                .sendAccount(66)
                .creator("yyyyc")
                .updater("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .deduplicationTime(1)
                .isNightShield(0)
                .build();

        MessageTemplate info = messageTemplateDao.save(messageTemplate);

        return JSON.toJSONString(info);

    }

    /**
     * test query
     */
    @GetMapping("/query")
    public String query() {
        Iterable<MessageTemplate> all = messageTemplateDao.findAll();
        for (MessageTemplate messageTemplate : all) {
            return JSON.toJSONString(messageTemplate);
        }
        return null;
    }
}
