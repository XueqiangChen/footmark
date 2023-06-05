package com.example.footmark.controller;

import com.example.footmark.service.SendKafkaMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/msg")
public class SendMsgController {

    @Autowired
    private SendKafkaMsgService sendKafkaMsgService;

    @RequestMapping(value = "/sendKafka", method = RequestMethod.GET)
    public String sendKafkaMsg(String content) {
        sendKafkaMsgService.publishMsg(content);
        return "";
    }
}
