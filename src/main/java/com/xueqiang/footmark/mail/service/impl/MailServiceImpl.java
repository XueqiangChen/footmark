package com.xueqiang.footmark.mail.service.impl;

import com.xueqiang.footmark.mail.service.MailService;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abc@qq.com");
        message.setTo("efd@qq.com");
        message.setSubject("Test send simple mail message");
        message.setText("Hello world!");

        javaMailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment() {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("to@qq.com");
            helper.setFrom("from@qq.com");
            helper.setSubject("Send attachment file to email");
            helper.setText("attachment file...");

            FileSystemResource file = new FileSystemResource(new File("Absolute path"));
            helper.addAttachment("Invoice", file);

            javaMailSender.send(message);
        } catch (MessagingException ex) {
            logger.error("Failed to send email to. error={}", ex.getMessage());
        }
    }

    @Override
    public void sendMessageWithCallback() {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO,
                    new InternetAddress("mail@qq.com"));
            mimeMessage.setFrom(new InternetAddress("from@qq.com"));
            mimeMessage.setText("Hello world!");
        };

        try {
            javaMailSender.send(preparator);
        }catch (MailException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendMessageWithFreemarkerTemplate() {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("to@qq.com");
            helper.setFrom("from@qq.com");
            helper.setSubject("Send freemarker template to email");

            HashMap<String, Object> models = new HashMap<>();
            models.put("name", "freemarker");

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("notification.flt");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setText(text);
            javaMailSender.send(message);
        } catch (Exception ex) {
            logger.error("Failed to send email to. error={}", ex.getMessage());
        }
    }
}
