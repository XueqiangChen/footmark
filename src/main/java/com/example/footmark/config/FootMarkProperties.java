package com.example.footmark.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
public class FootMarkProperties {

    @Value("${kafka.enable}")
    private String kafkaEnable;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootStraps;

    @Value("${kafka.produce.key.serializer}")
    private String kafkaKeyDeserializerClass;

    @Value("${kafka.produce.value.serializer}")
    private String kafkaValueDeserializerClass;

    @Value("${kafka.produce.acks}")
    private String kafkaAcks;

    @Value("${kafka.produce.max.block.ms}")
    private long kafkaMaxBlockMs;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Value("${kafka.consumer.group-id}")
    private String kafkaGroupId;
}
