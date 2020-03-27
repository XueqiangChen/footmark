package com.xueqiang.footmark.mail.service.impl;

import com.xueqiang.footmark.SmtpServerRule;
import com.xueqiang.footmark.mail.service.MailService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MailServiceImplTest {

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule();

    @Autowired
    private MailService mailService;

    @Test
    public void sendSimpleMessage() throws MessagingException {
        mailService.sendSimpleMessage();

        MimeMessage[] messages = smtpServerRule.getMessage();
        assertEquals("Test send simple mail message", messages[0].getSubject());
        assertEquals("abc@qq.com", messages[0].getFrom()[0].toString());
        assertEquals("efd@qq.com", messages[0].getAllRecipients()[0].toString());
    }

    @Test
    public void sendMessageWithAttachment() {
    }

    @Test
    public void sendMessageWithCallback() {
    }

    @Test
    public void sendMessageWithFreemarkerTemplate() {
    }
}
