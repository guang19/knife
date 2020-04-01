package com.github.guang19.knife.mail.sender;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.InputStream;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>邮件sender模板</p></p>
 */
public abstract class AbstractMailSender implements MailSender
{
    //mail会话
    private final Session mailSession;

    //邮件接受类型
    private final Message.RecipientType receiveType;

    /**
     * @param mailSession mail会话
     * @param receiveType 邮件接收类型
     */
    protected AbstractMailSender(Session mailSession, Message.RecipientType receiveType)
    {
        this.mailSession = mailSession;
        this.receiveType = receiveType;
    }


    /**
     * 创建通用邮件
     * @param subject       邮件标题
     * @param from          发送者
     * @param receivers     接受者
     * @return              邮件message
     * @throws MessagingException 邮件异常
     */
    private Message newCommonMailMessage(String subject , String from , String ...receivers) throws MessagingException
    {
        Message message = new MimeMessage(mailSession);
        message.setSubject(subject);
        message.setFrom(new InternetAddress(from));
        InternetAddress[] receiverAddress = new InternetAddress[receivers.length];
        for (int i = 0 ; i < receivers.length ; ++i)
        {
            receiverAddress[i] = new InternetAddress(receivers[i]);
        }
        message.addRecipients(receiveType,receiverAddress);
        return message;
    }


    /**
     * 创建普通文本邮件
     * @param subject   邮件标题
     * @param text      邮件正文文本内容
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     * @return          文本邮件message
     */
    protected Message createTextMessage(String subject, String text , String from , String ...receivers)
    {
        try
        {
            Message message = newCommonMailMessage(subject, from, receivers);
            message.setText(text);
            return message;
        }
        catch (MessagingException e)
        {
            throw new RuntimeException("an exception occurred while creating mail message.",e);
        }
    }

    /**
     * 创建带附件的文本邮件
     *
     * @param subject   邮件标题
     * @param text      邮件正文文本内容
     * @param file      附件
     * @param fileName  附件内容
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     * @return          文本邮件message
     */
    protected Message createTextMessageWithAttachment(String subject, String text, String file, String fileName, String from, String... receivers)
    {
        try
        {
            //设置附件源
            DataSource dataSource = new FileDataSource(new File(file));
            DataHandler dataHandler = new DataHandler(dataSource);
            return createTextMessageWithAttachment(subject,text,dataHandler,fileName,from,receivers);
        }
        catch (Exception e)
        {
            throw new RuntimeException("an exception occurred while creating mail message.",e);
        }
    }

    /**
     * 创建带附件的文本邮件
     * @param subject    邮件标题
     * @param text       邮件正文文本内容
     * @param fileStream 附件内容流
     * @param type       附件类型
     * @param fileName   附件名
     * @param from       发送者
     * @param receivers  接受者，允许接受多个接受者
     * @return           文本邮件message
     */
    protected Message createTextMessageWithAttachment(String subject, String text, InputStream fileStream, String type, String fileName, String from, String... receivers)
    {
        try
        {
            //设置附件源
            DataSource dataSource = new ByteArrayDataSource(fileStream,type);
            DataHandler dataHandler = new DataHandler(dataSource);
            return createTextMessageWithAttachment(subject,text,dataHandler,fileName,from,receivers);
        }
        catch (Exception e)
        {
            throw new RuntimeException("an exception occurred while creating mail message.",e);
        }
    }

    /**文本邮件message
     * 创建带附件的文本邮件
     * @param subject         邮件标题
     * @param text            邮件正文文本内容
     * @param dataHandler     附件源
     * @param fileName        附件名
     * @param from            发送者
     * @param receivers       接受者
     * @return                文本邮件message
     * @throws MessagingException   创建邮件时的异常
     */
    private Message createTextMessageWithAttachment(String subject, String text, DataHandler dataHandler, String fileName, String from, String... receivers)
            throws MessagingException
    {
        Message message = newCommonMailMessage(subject, from, receivers);
        BodyPart part1 = new MimeBodyPart();
        part1.setText(text);

        //设置附件资源
        BodyPart part2 = new MimeBodyPart();
        part2.setDataHandler(dataHandler);
        part2.setFileName(fileName);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);

        message.setContent(multipart);
        return message;
    }


    /**
     * 创建html邮件
     * @param subject   邮件标题
     * @param content   html邮件内容
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     * @return          html邮件message
     */
    protected Message createHtmlMessage(String subject, String content , String from , String ...receivers)
    {
        try
        {
            Message message = newCommonMailMessage(subject, from, receivers);
            message.setContent(content,"text/html;charset=utf-8");
            return message;
        }
        catch (MessagingException e)
        {
            throw new RuntimeException("an exception occurred while creating mail message.",e);
        }
    }


    /**
     * 创建带附件的html邮件
     * @param subject   邮件标题
     * @param content   html邮件内容
     * @param file      附件
     * @param fileName  附件名
     * @param from      发送者
     * @param receivers 接受者，允许接受多个接受者
     * @return html邮件message
     */
    protected Message createHtmlMessageWithAttachment(String subject, String content, String file, String fileName, String from, String... receivers)
    {
        try
        {
            //设置附件源
            DataSource dataSource = new FileDataSource(new File(file));
            DataHandler dataHandler = new DataHandler(dataSource);
            return createHtmlMessageWithAttachment(subject,content,dataHandler,fileName,from,receivers);
        }
        catch (Exception e)
        {
            throw new RuntimeException("an exception occurred while creating mail message.",e);
        }
    }


    /**
     * 发送带附件的html邮件
     * @param subject    邮件标题
     * @param content    html邮件内容
     * @param fileStream 附件内容流
     * @param type       附件类型
     * @param fileName   附件名
     * @param from       发送者
     * @param receivers  接受者，允许接受多个接受者
     * @return html邮件message
     */

    protected Message createHtmlMessageWithAttachment(String subject, String content, InputStream fileStream, String type, String fileName, String from, String... receivers)
    {
        try
        {
            //设置附件源
            DataSource dataSource = new ByteArrayDataSource(fileStream,type);
            DataHandler dataHandler = new DataHandler(dataSource);
            return createHtmlMessageWithAttachment(subject,content,dataHandler,fileName,from,receivers);
        }
        catch (Exception e)
        {
            throw new RuntimeException("an exception occurred while creating mail message.",e);
        }
    }


    /**
     * 创建带附件的html邮件
     * @param subject         邮件标题
     * @param content         html邮件内容
     * @param dataHandler     附件源
     * @param fileName        附件名
     * @param from            发送者
     * @param receivers       接受者
     * @return                html邮件message
     * @throws MessagingException   创建邮件时的异常
     */
    private Message createHtmlMessageWithAttachment(String subject, String content,DataHandler dataHandler, String fileName, String from, String... receivers)
            throws MessagingException
    {
        Message message = newCommonMailMessage(subject,from,receivers);

        BodyPart part1 = new MimeBodyPart();
        part1.setContent(content,"text/html;charset=utf-8");

        //设置附件资源
        BodyPart part2 = new MimeBodyPart();
        part2.setDataHandler(dataHandler);
        part2.setFileName(fileName);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);

        message.setContent(multipart);
        return message;
    }



    /**
     * 发送邮件
     * @param message 邮件
     */
    protected void sendMessage(Message message)
    {
        try
        {
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException("an exception occurred while sending mail message.",e);
        }
    }
}
