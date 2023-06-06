package com.example.footmark.service.impl;

import com.example.footmark.kafka.KafkaClientService;
import com.example.footmark.kafka.KafkaMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KafkaEventManager implements KafkaMessageConsumer {

    @Autowired
    private KafkaClientService kafkaClientService;
    private ExecutorService executorService;

//    @PostConstruct
    public void init() {
        log.info("Start kafka event manager");
        // Start consumer thread
        executorService = Executors.newFixedThreadPool(10);

        kafkaClientService.registerConsumer(this);
    }

    @PreDestroy
    public void destroy() {
        log.info("Stop executors");
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("InterruptedException on stopping executors", e);
        }
    }

    @Override
    public String getTopic() {
        return null;
    }

    @Override
    public void handleKafkaMessage(ConsumerRecord<Object, Object> msg) {
        // handle kafka message
    }
}
