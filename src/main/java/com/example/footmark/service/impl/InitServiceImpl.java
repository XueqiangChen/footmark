package com.example.footmark.service.impl;

import com.example.footmark.config.FootMarkProperties;
import com.example.footmark.kafka.KafkaClientService;
import com.example.footmark.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class InitServiceImpl implements InitService, ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private FootMarkProperties properties;

    @Autowired
    private KafkaClientService kafkaClientService;

    @Autowired
    private KafkaEventManager eventManager;

    @Override
    public void init() {
        if ("1".equals(properties.getKafkaEnable())) {
            eventManager.init();
            kafkaClientService.startProducerThread();
        }
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        init();
    }
}
