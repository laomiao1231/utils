package com.miao.mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class TestMail02 {
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    // 对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    public static String myEmailAccount = "taozishuotaozi@163.com";
    public static String myEmailPassword = "tao0126";
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.163.com";
    // 收件人邮箱（替换为自己知道的有效邮箱）
    public static String receiveMailAccount = "1578313952@qq.com";

    public static void main(String[] args) throws Exception {
        //连接邮件服务器参数配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");  //使用的协议
        props.setProperty("mail.smtp.host", myEmailSMTPHost);  //发送邮件使用SMTP服务器地址
        props.setProperty("mail.smtp.auth", "true");

        //根据配置创建会话， 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(true);  //查看详细的发送log
        //创建邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
        //根据session获取邮件传输对象
        Transport transport = session.getTransport();
        //使用邮箱账户和密码连接邮箱服务器， 这里认证的邮箱和message发件邮箱必须一致， 否则报错
        transport.connect(myEmailAccount, myEmailPassword);
        //发送邮件，发到所有收件地址
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();
    }

    /**
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        //发件人
        message.setFrom(new InternetAddress(sendMail, "miao", "UTF-8"));
        //收件人
        message.addRecipient(RecipientType.TO, new InternetAddress(receiveMail));
        //邮件主题
        message.setSubject("文本", "UTF-8");
        //创建图片节点
        MimeBodyPart image = new MimeBodyPart();
        DataHandler handler = new DataHandler(new FileDataSource("banner.jpg"));
        image.setDataHandler(handler);
        image.setContentID("image_banner");
        //创建文本节点
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("这是一张图片<br/><img width=400px src='cid:image_banner'>", "text/html;charset=UTF-8");
        //将文本与图片节点结合
        MimeMultipart text_image = new MimeMultipart();
        text_image.addBodyPart(text);
        text_image.addBodyPart(image);
        text_image.setSubType("related");
        // 将 文本+图片 的混合“节点”封装成一个普通“节点”，
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart,
        // 所以我们需要的是 BodyPart,上面的 text_image 并非 BodyPart,所有要把text_image封装成一个BodyPart
        MimeBodyPart fix_text_image = new MimeBodyPart();
        fix_text_image.setContent(text_image);
        //创建附件节点
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler handler1 = new DataHandler(new FileDataSource("目录.doc"));
        attachment.setDataHandler(handler1);
        attachment.setFileName(MimeUtility.encodeText(handler1.getName())); //设置附件的文件名
        //设置（文本+图片）和 附件 的关系（合成一个大的混合“节点”）
        MimeMultipart content = new MimeMultipart();
        content.addBodyPart(fix_text_image);
        content.addBodyPart(attachment);  //如果有多个附件，可创建多个多次添加
        content.setSubType("mixed");
        //设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
        //邮件内容
        message.setContent(content);
        //邮件发送时间
        message.setSentDate(new Date());
        //保存设置
        message.saveChanges();
        return message;
    }
}
