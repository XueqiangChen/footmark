package com.example.footmark.kafka.impl;

import com.example.footmark.config.FootMarkProperties;
import com.example.footmark.kafka.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KafkaClientServiceImpl implements KafkaClientService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaClientServiceImpl.class);

	@Autowired
	private FootMarkProperties properties;

	private final KafkaProducerClient kafkaProducerClient = new KafkaProducerClient();

    // As whole consumer group to share messages in a topic
	private final KafkaConsumerClient kafkaConsumerGroupClient = new KafkaConsumerClient();

    // As individual subscriber to receive every message in a topic
	private final KafkaConsumerClient kafkaTopicConsumerClient = new KafkaConsumerClient() ;

	@Override
	public void startProducerThread() {
		logger.info("start kafka producer thread...");
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafkaBootStraps());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getKafkaKeyDeserializerClass());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, properties.getKafkaValueDeserializerClass());
        props.put(ProducerConfig.ACKS_CONFIG, properties.getKafkaEnable());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, properties.getKafkaMaxBlockMs());

        String logData = props.toString();
		logger.info("create kafka producer client with props [" + logData + "]");
		kafkaProducerClient.init(props);
		kafkaProducerClient.start();
	}

	@Override
	public void startConsumerThread() {
		logger.info("start kafka consumer thread...");
        initConsumerGroup();
        initTopicConsumer();

	}

    @Override
    public void registerConsumer(KafkaMessageConsumer consumer) {
        KafkaTopic kafkaTopic = Arrays.stream(KafkaTopic.values())
                .filter(v-> !v.getType().equals(KafkaTopicType.PRODUCER.getType()))
                .filter(v-> v.getTopic().equals(consumer.getTopic())).findFirst().get();
        if (kafkaTopic.getType().equals(KafkaTopicType.CONSUMER.getType()) ) {
            logger.info("Register topic consumer for {}", consumer.getTopic());
            kafkaTopicConsumerClient.registerConsumer(consumer);
        } else {
            logger.info("Register group consumer for {}", consumer.getTopic());
            kafkaConsumerGroupClient.registerConsumer(consumer);
        }
    }

    private Properties createConsumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafkaBootStraps());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getKafkaGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getKafkaKeyDeserializerClass());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getKafkaValueDeserializerClass());
        return props;
    }

    private void initConsumerGroup() {
        Properties props = createConsumerProperties();
        String logData = props.toString();
        logger.info("create kafka consumer group client with props [" + logData + "]");

        List<String> consumerGroupTopics = new ArrayList<>();
        Arrays.asList(KafkaTopic.values()).stream().forEach((KafkaTopic v) -> {
            if (StringUtils.equals(v.getType(), KafkaTopicType.CONSUMER_GROUP.getType())) {
                consumerGroupTopics.add(v.getTopic());
            }
        });
        if (consumerGroupTopics.isEmpty()) {
            logger.info("no consumer group topics to be consumed...");
            return;
        }
        logData = consumerGroupTopics.toString();
        logger.info("kafka consumer group topics: " + logData);

        kafkaConsumerGroupClient.init(props);
        kafkaConsumerGroupClient.setTopics(consumerGroupTopics);
        kafkaConsumerGroupClient.start();
    }

    private void initTopicConsumer() {
        Properties props = createConsumerProperties();
        // Use unique consumer groupId
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getKafkaGroupId()+ UUID.randomUUID().toString());
        String logData = props.toString();
        logger.info("create kafka topic consumer client with props [" + logData + "]");

        List<String> consumerTopics = new ArrayList<>();
        Arrays.asList(KafkaTopic.values()).stream().forEach(v->{
            if( StringUtils.equals(v.getType(), KafkaTopicType.CONSUMER.getType())) {
                consumerTopics.add(v.getTopic());
            }
        });

        if (consumerTopics.isEmpty()) {
            logger.info("no consumer topics to be consumed...");
            return;
        }
        logData = consumerTopics.toString();
        logger.info("kafka consumer topics: " + logData);

        kafkaTopicConsumerClient.init(props);
        kafkaTopicConsumerClient.setTopics(consumerTopics, true);
        kafkaTopicConsumerClient.start();
    }

    @Override
	public void sendMsg(ProducerRecord<Object, Object> msg) {
		if (kafkaProducerClient == null) {
			logger.error("kafka producer thread is not start..., won't send msg");
		} else {
			kafkaProducerClient.sendMsg(msg);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ProducerRecord<Object, Object> createProducerMsg(String topic, Object key, Object value) {
		return new ProducerRecord(topic, key, value);
	}

}
