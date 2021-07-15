package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String mailUsername;

    // 注入发送 Mail 类
    @Resource
    private JavaMailSender javaMailSender;

    // 模板
    @Resource
    private TemplateEngine templateEngine;

    /**
     * 发送确认链接到注册者邮箱
     *
     * @param activationUrl     激活 url 地址
     * @param ToEmail           收件人邮箱
     */
    public void sendMailForActivationAccount(String activationUrl, String ToEmail) {
        // 创建对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);
            messageHelper.setSubject("欢迎注册 Jianxiao.com   - 个人激活账号");           // 设置邮件主题
            messageHelper.setFrom(mailUsername);                                        // 设置邮件发送人
            messageHelper.setTo(ToEmail);                                               // 设置邮件收件人
            messageHelper.setSentDate(new Date());                                      // 设置邮件发送日期
            // 创建上下文
            Context context = new Context();
            context.setVariable("activationUrl",activationUrl);                   // 设置变量
            String text =
                    templateEngine.process("activation-account.html", context); // 替换模板
            messageHelper.setText(text,true);                                       // 设置发送内容
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);                                                 // 发送邮件

    }
}
