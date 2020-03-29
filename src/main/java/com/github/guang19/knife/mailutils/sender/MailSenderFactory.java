package com.github.guang19.knife.mailutils.sender;

import com.github.guang19.knife.mailutils.sender.MailSender;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>MailSender工厂</p>
 */
public interface MailSenderFactory
{
    /**
     * 获取MailSender
     * @return  MailSender
     */
    public abstract MailSender getMailSender();
}