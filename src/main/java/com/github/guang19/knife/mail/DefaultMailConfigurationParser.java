package com.github.guang19.knife.mail;

import com.github.guang19.knife.mail.exceptions.MailConfigurationException;

import javax.mail.Message;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>邮箱配置默认解析器</p>
 */
public class DefaultMailConfigurationParser implements MailConfigurationParser
{
    //邮箱配置文件
    private String configuration;

    /**
     * @param configuration 邮箱配置文件
     */
    public DefaultMailConfigurationParser(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * 解析并返回邮箱配置
     * @return MailConfiguration
     * @throws Exception 解析配置过程中的异常
     */
    @Override
    public MailConfiguration parse() throws Exception
    {
        return parse(configuration);
    }

    //解析邮箱配置文件
    private MailConfiguration parse(String configuration) throws Exception
    {
        InputStream propertiesInputStream = ClassLoader.getSystemResourceAsStream(configuration);
        if(propertiesInputStream == null)
        {
            throw new NullPointerException("cannot loading mail configuration.");
        }
        Properties mailProperties = new Properties();
        mailProperties.load(propertiesInputStream);
        //检查必需配置
        checkedRequiredProperties(mailProperties);
        //设置默认属性
        setDefaultMailProperties(mailProperties);
        MailConfiguration mailConfiguration = new MailConfiguration(mailProperties.getProperty(BaseMailConfiguration.USERNAME_KEY),mailProperties.getProperty(BaseMailConfiguration.PASSWORD_KEY));
        mailConfiguration.setProperties(mailProperties);
        //设置邮件接收类型
        mailConfiguration.setReceiveType(mailProperties.getProperty(BaseMailConfiguration.RECEIVE_TYPE_KEY).equals("cc") ? Message.RecipientType.CC :
                (mailProperties.getProperty(BaseMailConfiguration.RECEIVE_TYPE_KEY).equals("bcc") ? Message.RecipientType.BCC : Message.RecipientType.TO));
        return mailConfiguration;
    }

    //检查必需配置
    private void checkedRequiredProperties(Properties mailProperties)
    {
        if(mailProperties.getProperty(BaseMailConfiguration.USERNAME_KEY) == null || mailProperties.getProperty(BaseMailConfiguration.PASSWORD_KEY) == null)
        {
            throw new MailConfigurationException("mail username or password cannot are not configured.");
        }
        if(mailProperties.getProperty("mail.smtp.host") == null || mailProperties.getProperty("mail.smtp.port") == null)
        {
            throw new MailConfigurationException("mail host or host's port cannot are not configured.");
        }
        else
        {
            mailProperties.setProperty("mail.smtp.socketFactory.port",mailProperties.getProperty("mail.smtp.port"));
        }
    }

    //设置常用的缺省的默认属性
    private void setDefaultMailProperties(Properties mailProperties)
    {
        if(mailProperties.getProperty(BaseMailConfiguration.RECEIVE_TYPE_KEY) == null)
        {
            mailProperties.setProperty("mail.receive-type","to");
        }
        if(mailProperties.getProperty("mail.smtp.auth") == null)
        {
            mailProperties.setProperty("mail.smtp.auth","true");
        }
        if(mailProperties.getProperty("mail.transport.protocol") == null)
        {
            mailProperties.setProperty("mail.transport.protocol","smtp");
        }
        if(mailProperties.getProperty("mail.smtp.ssl.enable") == null)
        {
            mailProperties.setProperty("mail.smtp.ssl.enable","true");
        }
        if(mailProperties.getProperty("mail.smtp.starttls.enable") == null)
        {
            mailProperties.setProperty("mail.smtp.starttls.enable","true");
        }
        if(mailProperties.getProperty("mail.smtp.starttls.required") == null)
        {
            mailProperties.setProperty("mail.smtp.starttls.required","true");
        }
        if(mailProperties.getProperty("mail.smtp.socketFactory.class") == null)
        {
            mailProperties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        }
        if(mailProperties.getProperty("mail.smtp.socketFactory.fallback") == null)
        {
            mailProperties.setProperty("mail.smtp.socketFactory.fallback","false");
        }
        if(mailProperties.getProperty("mail.smtp.ssl.socketFactory.class") == null)
        {
            mailProperties.setProperty("mail.smtp.ssl.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        }
    }
}
