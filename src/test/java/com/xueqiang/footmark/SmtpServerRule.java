package com.xueqiang.footmark;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.rules.ExternalResource;

import javax.mail.internet.MimeMessage;

public class SmtpServerRule extends ExternalResource {

    private static final String USER_PASSWORD = "password";
    private static final String USER_NAME = "abc@qq.com";

    private GreenMail smtpServer;

    @Override
    protected void before() throws Throwable {
        super.before();
        smtpServer = new GreenMail(ServerSetupTest.SMTP);
        smtpServer.start();
        // setup user on the mail server
        smtpServer.setUser(USER_NAME, USER_PASSWORD);
    }

    public MimeMessage[] getMessage() {
        return smtpServer.getReceivedMessages();
    }

    @Override
    protected void after() {
        super.after();
        smtpServer.stop();
    }
}
