package com.github.guang19.knife.mail.sender.impl;

import com.github.guang19.knife.mail.sender.AbstractMailSender;

import javax.mail.Message;
import javax.mail.Session;
import java.io.InputStream;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>默认的邮箱sender</p>
 */
public class DefaultMailSender extends AbstractMailSender
{
    /**
     * mail session
     * @param mailSession   mail会话
     */
    /**
     * @param mailSession mail会话
     * @param receiveType 邮件接收类型
     */
    public DefaultMailSender(Session mailSession, Message.RecipientType receiveType)
    {
        super(mailSession, receiveType);
    }

    /**
     * 发送普通文本邮件
     *
     * @param subject   邮件标题
     * @param text      邮件正文文本内容
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     */
    @Override
    public void sendTextMessage(String subject, String text, String from, String... receivers)
    {
        sendMessage(createTextMessage(subject, text, from, receivers));
    }

    /**
     * 发送html邮件
     *
     * @param subject   邮件标题
     * @param content   html邮件内容
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     */
    @Override
    public void sendHtmlMessage(String subject, String content, String from, String... receivers)
    {
        sendMessage(createHtmlMessage(subject, content, from, receivers));
    }

    /**
     * 发送带附件的文本邮件
     *
     * @param subject   邮件标题
     * @param text      邮件正文文本内容
     * @param file      附件
     * @param fileName  附件内容
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     */
    @Override
    public void sendTextMessageWithAttachment(String subject, String text, String file, String fileName, String from, String... receivers)
    {
        sendMessage(createTextMessageWithAttachment(subject, text, file, fileName, from, receivers));
    }

    /**
     * 发送带附件的文本邮件
     *
     * @param subject    邮件标题
     * @param text       邮件正文文本内容
     * @param fileStream 附件内容流
     * @param type       附件类型
     * @param fileName   附件名
     * @param from       发送者
     * @param receivers  接受者，允许接受多个接受者
     */
    @Override
    public void sendTextMessageWithAttachment(String subject, String text, InputStream fileStream, String type, String fileName, String from, String... receivers)
    {
        sendMessage(createTextMessageWithAttachment(subject, text, fileStream, type, fileName, from, receivers));
    }

    /**
     * 发送带附件的html邮件
     *
     * @param subject   邮件标题
     * @param content   html邮件内容
     * @param file      附件
     * @param fileName  附件名
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     */
    @Override
    public void sendHtmlMessageWithAttachment(String subject, String content, String file, String fileName, String from, String... receivers)
    {
        sendMessage(createHtmlMessageWithAttachment(subject, content, file, fileName, from, receivers));
    }

    /**
     * 发送带附件的html邮件
     *
     * @param subject    邮件标题
     * @param content    html邮件内容
     * @param fileStream 附件内容流
     * @param type       附件类型
     * @param fileName   附件名
     * @param from       发送者
     * @param receivers  接受者，允许接受多个接受者
     */
    @Override
    public void sendHtmlMessageWithAttachment(String subject, String content, InputStream fileStream, String type, String fileName, String from, String... receivers)
    {
        sendMessage(createHtmlMessageWithAttachment(subject, content, fileStream, type, fileName, from, receivers));
    }
}
