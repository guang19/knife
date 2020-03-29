package com.github.guang19.knife.mailutils;

import javax.mail.Message;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>mail基础配置</p>
 */
public class BaseMailConfiguration
{
    //username配置属性key
    public static final String USERNAME_KEY = "mail.username";

    //password配置属性key
    public static final String PASSWORD_KEY = "mail.password";

    //receive type 配置属性key
    public static final String RECEIVE_TYPE_KEY = "mail.receive-type";

    //邮箱地址
    private final String username;

    //邮箱授权码
    private final String password;

    //接收邮件的类型
    private Message.RecipientType receiveType;

    /**
     *
     * @param username 邮箱地址
     * @param password 邮箱授权码
     */
    public BaseMailConfiguration(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    /**
     * getter
     * @return password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * getter
     * @return username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * setter
     * @param receiveType 接收邮件的类型
     */
    public void setReceiveType(Message.RecipientType receiveType)
    {
        this.receiveType = receiveType;
    }

    /**
     * getter
     * @return  receiveType 接收邮件的类型
     */
    public Message.RecipientType getReceiveType()
    {
        return receiveType;
    }
}
