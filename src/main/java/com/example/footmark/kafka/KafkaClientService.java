package com.example.footmark.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaClientService {
    void startProducerThread();

    void startConsumerThread();

    void sendMsg(ProducerRecord<Object, Object> msg);

    ProducerRecord<Object, Object> createProducerMsg(String topic, Object key, Object value);

    void registerConsumer(KafkaMessageConsumer consumer);
}
