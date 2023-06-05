package com.example.footmark.service.impl;

import com.example.footmark.config.FootMarkProperties;
import com.example.footmark.kafka.KafkaClientService;
import com.example.footmark.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class InitServiceImpl implements InitService, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FootMarkProperties properties;

    @Autowired
    private KafkaClientService kafkaClientService;

    @Override
    public void init() {
        if ("1".equals(properties.getKafkaEnable())) {
            kafkaClientService.startProducerThread();
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }
}
