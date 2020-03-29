package com.github.guang19.knife;

import com.github.guang19.knife.mailutils.sender.DefaultMailSenderFactory;
import com.github.guang19.knife.mailutils.sender.DefaultMailSenderFactoryBuilder;
import com.github.guang19.knife.mailutils.sender.MailSender;
import com.github.guang19.knife.mailutils.sender.impl.DefaultMailSender;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * @author yangguang
 * @date 2020/3/28
 * @description <p></p>
 */
public class MainUtilsTest
{
    Properties properties = new Properties();

    Session session;



    @Before
    public void before()
    {
        properties.setProperty("mail.smtp.user","2196927727@qq.com");
        properties.setProperty("mail.smtp.host","smtp.qq.com");
        properties.setProperty("mail.smtp.port","465");
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.ssl.enable","true");
        properties.setProperty("mail.smtp.starttls.enable","true");
        properties.setProperty("mail.smtp.starttls.required","true");
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback","false");
        properties.setProperty("mail.smtp.ssl.socketFactory.class","javax.net.ssl.SSLSocketFactory");

        session = Session.getDefaultInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("2196927727@qq.com","racvkrqgtpzbeafd");
            }
        });
    }

    //发送简单邮件
    @Test
    public void tes01() throws Throwable
    {
        MimeMessage mimeMessage  = new MimeMessage(session);
        mimeMessage.setFrom("2196927727@qq.com");
        mimeMessage.addRecipients(Message.RecipientType.TO,
                new InternetAddress[]{new InternetAddress("2196927727@qq.com"),new InternetAddress("1446125917@qq.com")});
        mimeMessage.setSubject("简单邮件标题");
        mimeMessage.setText("");

        Transport.send(mimeMessage);
    }

    //发送html内容的邮件
    @Test
    public void test02() throws Throwable
    {
        MimeMessage mimeMessage  = new MimeMessage(session);
        mimeMessage.setFrom("2196927727@qq.com");
        mimeMessage.addRecipients(Message.RecipientType.TO,
                new InternetAddress[]{new InternetAddress("2196927727@qq.com"),new InternetAddress("1446125917@qq.com")});
        mimeMessage.setSubject("简单邮件标题");
        mimeMessage.setContent("<h1>html邮件</h1>","text/html;charset=utf-8");
        Transport.send(mimeMessage);
    }

    //发送带有附件的邮件
    @Test
    public void test03() throws Throwable
    {

        MimeMessage mimeMessage  = new MimeMessage(session);
        mimeMessage.setFrom("2196927727@qq.com");
        mimeMessage.addRecipients(Message.RecipientType.TO,
                "2196927727@qq.com");
        mimeMessage.setSubject("简单邮件标题");

        BodyPart bodyPart1 = new MimeBodyPart();
//        bodyPart1.setText("附件内容");

        bodyPart1.setContent("<h1>html邮件</h1>","text/html;charset=utf-8");

        BodyPart bodyPart2 = new MimeBodyPart();
        DataSource dataSource = new FileDataSource("/home/yangguang/下载/tcp-ip-.pdf");

//        FileInputStream fileInputStream = new FileInputStream("/home/yangguang/下载/tcp-ip-.pdf");

//        DataSource dataSource = new ByteArrayDataSource(fileInputStream,"text/pdf");
        DataHandler dataHandler = new DataHandler(dataSource);
        bodyPart2.setDataHandler(dataHandler);
        bodyPart2.setFileName("tcp-ip");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart1);
        multipart.addBodyPart(bodyPart2);

        mimeMessage.setContent(multipart);

        Transport.send(mimeMessage);
    }

    @Test
    public void test04() throws Exception
    {
        DefaultMailSenderFactory mailSenderFactory = (DefaultMailSenderFactory) new DefaultMailSenderFactoryBuilder("mail.properties").build();
        DefaultMailSender mailSender = (DefaultMailSender)mailSenderFactory.getMailSender();
//        mailSender.sendTextMessage("简单邮件标题","简单邮件","2196927727@qq.com","2196927727@qq.com");

//        mailSender.sendHtmlMessage("简单邮件标题","<h1>简单邮件</h1>","2196927727@qq.com","2196927727@qq.com");

//        mailSender.sendTextMessageWithAttachment("简单邮件标题","简单邮件","src/main/java/a.txt","a","2196927727@qq.com","2196927727@qq.com");

//        mailSender.sendTextMessageWithAttachment("简单邮件标题","简单邮件",new FileInputStream("/home/yangguang/下载/tcp-ip-.pdf"),"text/pdf","tcp/ip","2196927727@qq.com","2196927727@qq.com");

//        mailSender.sendHtmlMessageWithAttachment("简单邮件标题","<h2>简单邮件</h2>","src/main/java/a.txt","a","2196927727@qq.com","2196927727@qq.com");

//        mailSender.sendHtmlMessageWithAttachment("简单邮件标题","<h2>简单邮件</h2>",new FileInputStream("/home/yangguang/下载/tcp-ip-.pdf"),"text/pdf","tcp/ip","2196927727@qq.com","2196927727@qq.com");
    }
}