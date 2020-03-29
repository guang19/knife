package com.github.guang19.knife.mailutils;

import java.util.Properties;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>mail邮件配置</p>
 */
public class MailConfiguration extends BaseMailConfiguration
{
    //java mail属性
    private Properties properties;

    /**
     * @param username  用户名
     * @param password  授权码
     */
    public MailConfiguration(String username,String password)
    {
        super(username,password);
    }

    /**
     * setter
     * @param properties    java mail属性
     */
    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    /**
     * getter
     * @return     java mail属性
     */
    public Properties getProperties()
    {
        return properties;
    }
}
