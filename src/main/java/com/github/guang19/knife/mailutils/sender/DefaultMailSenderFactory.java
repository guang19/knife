package com.github.guang19.knife.mailutils.sender;

import com.github.guang19.knife.mailutils.MailConfiguration;
import com.github.guang19.knife.mailutils.sender.impl.DefaultMailSender;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>默认MailSender工厂</p>
 */
public class DefaultMailSenderFactory implements MailSenderFactory
{

    //邮箱配置
    private MailConfiguration mailConfiguration;

    /**
     * @param mailConfiguration 邮箱配置
     */
    public DefaultMailSenderFactory(MailConfiguration mailConfiguration)
    {
        this.mailConfiguration = mailConfiguration;
    }

    /**
     * 获取MailSender
     *
     * @return MailSender
     */
    @Override
    public MailSender getMailSender()
    {
        return newDefaultMailSenderFactory(mailConfiguration);
    }

    //创建默认的MailSender
    private DefaultMailSender newDefaultMailSenderFactory(MailConfiguration mailConfiguration)
    {
        Session session = Session.getInstance(mailConfiguration.getProperties(),
                new Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(mailConfiguration.getUsername(),mailConfiguration.getPassword());
                    }
                });
        return new DefaultMailSender(session,mailConfiguration.getReceiveType());
    }
}
