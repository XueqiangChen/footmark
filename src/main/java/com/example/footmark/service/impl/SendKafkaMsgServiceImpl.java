package com.example.footmark.service.impl;

import com.example.footmark.kafka.KafkaClientService;
import com.example.footmark.kafka.KafkaTopic;
import com.example.footmark.service.SendKafkaMsgService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendKafkaMsgServiceImpl implements SendKafkaMsgService {

    private final Logger logger = LoggerFactory.getLogger(SendKafkaMsgServiceImpl.class);

    private final String kafkaTopic = KafkaTopic.HELLO.getTopic();

    private final String kafkaMessageKey = KafkaTopic.HELLO.getKey();

    @Autowired
    private KafkaClientService kafkaClientService;

    @Override
    public void publishMsg(String content) {
        logger.info("Publish event to Kafka Topic {}: content {}", kafkaTopic, content);
        ProducerRecord<Object, Object> record = kafkaClientService.createProducerMsg(kafkaTopic, kafkaMessageKey,
                content);

        kafkaClientService.sendMsg(record);
    }
}
