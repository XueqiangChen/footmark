package com.xueqiang.footmark.mail.service;


public interface MailService {

    void sendSimpleMessage();

    void sendMessageWithAttachment();

    void sendMessageWithCallback();

    void sendMessageWithFreemarkerTemplate();
}
