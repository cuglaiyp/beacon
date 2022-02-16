package com.onebit.beacon.handler;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.onebit.beacon.domain.TaskInfo;
import com.onebit.beacon.dto.EmailContentModel;
import com.onebit.beacon.enums.ChannelType;
import com.onebit.beacon.util.AccountUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Onebit
 * @Date: 2022/1/17
 */
@Slf4j
@Component
public class EmailHandler extends BaseHandler {

    private static final String EMAIL_ACCOUNT_KEY = "emailAccount";
    private static final String PREFIX = "email_";

    @Autowired
    private AccountUtil accountUtil;

    public EmailHandler() {
        this.channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handle(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount accountConfig = getAccountConfig(taskInfo.getSendAccount());
        try {
            MailUtil.send(accountConfig, taskInfo.getReceiver(),
                    emailContentModel.getTitle(), emailContentModel.getTitle(),
                    true, null);
        } catch (Exception e) {
            log.error("EmailHandler#handle fail! {}, params: {}",
                    Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    private MailAccount getAccountConfig(Integer sendAccount) {
        MailAccount account = accountUtil.getAccount(sendAccount, EMAIL_ACCOUNT_KEY, PREFIX, new MailAccount());
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(true)
                    .setStarttlsEnable(true)
                    .setSslEnable(true)
                    .setCustomProperty("mail.smtp.ssl.socketFactory", sf)
                    .setTimeout(25000)
                    .setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccountConfig fail! {}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }
}
