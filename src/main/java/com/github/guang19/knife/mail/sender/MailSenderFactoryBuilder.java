package com.github.guang19.knife.mail.sender;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>MailSenderFactory构造器</p>
 */
public interface MailSenderFactoryBuilder
{
    /**
     * 构造MailSenderFactory
     * @return  MailSenderFactory
     */
    public abstract MailSenderFactory build();
}
