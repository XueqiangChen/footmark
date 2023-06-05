package com.example.footmark.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaMessageConsumer {

    String getTopic();

    void handleKafkaMessage(ConsumerRecord<Object, Object> msg);
}
