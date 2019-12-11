package com.xueqiang.footmark.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaClientServiceTest {

    @Autowired
    KafkaClientService kafkaClientService;

    @Test
    public void testInitConfig() {
        kafkaClientService.initConfig();
    }

    @Test
    public void testSendMsg() {
        kafkaClientService.sendMsg("Hello Kafka!");
    }
}