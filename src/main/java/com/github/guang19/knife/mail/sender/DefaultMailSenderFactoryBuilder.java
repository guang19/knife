package com.github.guang19.knife.mail.sender;

import com.github.guang19.knife.mail.DefaultMailConfigurationParser;
import com.github.guang19.knife.mail.MailConfiguration;
import com.github.guang19.knife.mail.MailConfigurationParser;
import com.github.guang19.knife.mail.exceptions.MailConfigurationException;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>构造DefaultMailSenderFactory</p>
 */
public class DefaultMailSenderFactoryBuilder implements MailSenderFactoryBuilder
{
    //邮箱配置文件
    private String configuration;

    /**
     * @param configuration 配置文件
     */
    public DefaultMailSenderFactoryBuilder(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * 构造DefaultMailSenderFactory
     *
     * @return MailSenderFactory
     */
    @Override
    public MailSenderFactory build()
    {
        return buildDefaultMailSenderFactory(configuration);
    }

    ///根据配置文件构造DefaultMailSenderFactory
    private DefaultMailSenderFactory buildDefaultMailSenderFactory(String configuration)
    {
        try
        {
            MailConfigurationParser mailConfigurationParser = new DefaultMailConfigurationParser(configuration);
            MailConfiguration mailConfiguration = mailConfigurationParser.parse();
            return new DefaultMailSenderFactory(mailConfiguration);
        }
        catch (Exception e)
        {
            throw new MailConfigurationException("an exception occurred while loading the mail configuration.", e);
        }
    }

}
