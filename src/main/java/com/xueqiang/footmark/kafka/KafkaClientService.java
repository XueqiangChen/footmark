package com.xueqiang.footmark.kafka;

import com.xueqiang.footmark.config.FootmarkProperties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class KafkaClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaClientService.class);
    private static final String TOPIC = "topic-demo";
    private static final String CLIENT_ID = "producer.client.id.demo";
    private static final int RETRIES_TIMES = 10;

    @Autowired
    private FootmarkProperties properties;

    public Properties initConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafkaBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        // add retry times
        props.put(ProducerConfig.RETRIES_CONFIG, RETRIES_TIMES);

        LOGGER.info("init kafka config {}", props);
        return props;
    }

    public void sendMsg(String message) {
        Properties props = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);

        try{
            producer.send(record);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        producer.close();
    }

    // Synchronize send msg
    public void sendMsgSync(String message) {
        Properties props = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);

        try{
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get();
            LOGGER.info("metadata={}", metadata);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }

        producer.close();
    }

    // asynchronous send msg
    public void sendMsgAsync(String message) {
        Properties props = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);

        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    // handle exception
                    LOGGER.error("send msg async failed, cause by {}", exception.getMessage());
                } else {
                    LOGGER.info("send msg async success! metadata={}", metadata);
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        producer.close();
    }
}
