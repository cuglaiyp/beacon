package com.onebit.beacon.vo.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.onebit.beacon.constant.BeaconConstant;
import com.onebit.beacon.dao.MessageTemplateDao;
import com.onebit.beacon.domain.MessageTemplate;
import com.onebit.beacon.enums.AuditStatus;
import com.onebit.beacon.enums.MessageStatus;
import com.onebit.beacon.service.MessageTemplateService;
import com.onebit.beacon.vo.MessageTemplateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    public List<MessageTemplate> queryList(MessageTemplateParam messageTemplateParam) {
        PageRequest pageRequest = PageRequest.of(messageTemplateParam.getPage() - 1, messageTemplateParam.getPerPage());
        return messageTemplateDao.findAllByIsDeletedEquals(BeaconConstant.FALSE, pageRequest);
    }

    @Override
    public Long count() {
        return messageTemplateDao.countByIsDeletedEquals(BeaconConstant.FALSE);
    }

    @Override
    public MessageTemplate saveOrUpdate(MessageTemplate messageTemplate) {
        if (messageTemplate.getId() == null) {
            initStatus(messageTemplate);
        }
        return messageTemplateDao.save(messageTemplate);
    }

    private void initStatus(MessageTemplate messageTemplate) {
        messageTemplate.setFlowId(StrUtil.EMPTY)
                .setMessageStatus(MessageStatus.INIT.getCode())
                .setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("onebit")
                .setUpdater("onebit")
                .setTeam("公众号onebit")
                .setAuditor("mess")
                .setDeduplicationTime(BeaconConstant.FALSE)
                .setIsNightShield(BeaconConstant.FALSE)
                .setCreated(Math.toIntExact(DateUtil.currentSeconds()))
                .setUpdated(Math.toIntExact(DateUtil.currentSeconds()))
                .setIsDeleted(BeaconConstant.FALSE);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        List<MessageTemplate> messageTemplates = messageTemplateDao.findAllById(ids);
        messageTemplates.forEach(m -> m.setIsDeleted(BeaconConstant.TRUE));
        messageTemplateDao.saveAll(messageTemplates);
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return messageTemplateDao.findById(id).get();
    }

    @Override
    public void copy(Long id) {
        MessageTemplate messageTemplate = messageTemplateDao.findById(id).get();
        MessageTemplate clone = ObjectUtil.clone(messageTemplate);
        clone.setId(null);
        messageTemplateDao.save(clone);
    }
}
