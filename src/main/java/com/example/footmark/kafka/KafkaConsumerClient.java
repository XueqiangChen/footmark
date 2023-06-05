package com.example.footmark.kafka;

import com.example.footmark.util.ThreadUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

//@Component
public class KafkaConsumerClient extends KafkaClientBase {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerClient.class);

	private KafkaConsumer<Object, Object> consumer;
	private LinkedBlockingQueue<ConsumerRecord<Object, Object>> queue;

	private volatile boolean exitFlag = false;

	private ConcurrentHashMap<String, KafkaMessageConsumer> consumerMap = new ConcurrentHashMap<>();

	public KafkaConsumerClient() {
		//
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void init(Properties props) {
		consumer = new KafkaConsumer(props);
	}

	public void setTopics(List<String> topics) {
		setTopics(topics, false);
	}

	public void setTopics(List<String> topics, boolean seekToEnd) {
		consumer.subscribe(topics);
		if( seekToEnd ) {
		    // As we want to consumer from latest messages, we can ignore any existing message
            // use poll to get topicPartitions
            consumer.poll(0);
            Set<TopicPartition> topicPartitions = consumer.assignment();
            if( topicPartitions.size() > 0 ) {
                logger.info("Seek to end for topic partitions {}", topicPartitions.toString());
                consumer.seekToEnd(topicPartitions);
            } else {
                logger.warn("No topic partitions found.");
            }
        }
	}

	public void registerConsumer(KafkaMessageConsumer consumer) {
	    consumerMap.put(consumer.getTopic(), consumer);
    }


	public void start() {
		queue = new LinkedBlockingQueue<>(1024);
		handleExecutor = Executors.newSingleThreadExecutor(ThreadUtils.createThreadFactory("kafka-consumer-thread"));
		handleExecutor.submit(new KafkaConsumerTask(consumer));
		ExecutorService msgHandleExecutor = Executors
				.newSingleThreadExecutor(ThreadUtils.createThreadFactory("kafka-msg-handle-thread"));
		msgHandleExecutor.submit(new ConsumerMsgHandleTask());
	}


	public void shutdown() {
		exitFlag = true;
		super.stopExecutor();
		if (consumer != null) {
			consumer.close();
		}
	}


	public void handleMsg(ConsumerRecord<Object, Object> msg) {
	    KafkaMessageConsumer consumer = consumerMap.get(msg.topic());
	    if( consumer == null ) {
	        consumer = defaultMessageConsumer;
        }
	    consumer.handleKafkaMessage(msg);
	}

	private static KafkaMessageConsumer defaultMessageConsumer = new KafkaMessageConsumer() {
        @Override
        public String getTopic() {
            return null;
        }

        @Override
        public void handleKafkaMessage(ConsumerRecord<Object, Object> msg) {
            String data = msg.toString();
            logger.info("handle kafka msg [" + data + "]");
        }
    };

	class KafkaConsumerTask implements Runnable {
		private KafkaConsumer<Object, Object> consumer;

		public KafkaConsumerTask(KafkaConsumer<Object, Object> consumer) {
			this.consumer = consumer;
		}

		@Override
		public void run() {
			logger.info(Thread.currentThread().getName() + " started");
			while (!exitFlag) {
				try {
					ConsumerRecords<Object, Object> records = consumer.poll(10000);
					for (ConsumerRecord<Object, Object> record : records) {
						queue.put(record);
						consumer.commitSync();
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			logger.info(Thread.currentThread().getName() + " finished");
		}
	}

	class ConsumerMsgHandleTask implements Runnable {
		@Override
		public void run() {
			logger.info(Thread.currentThread().getName() + " started");
			while (!exitFlag) {
				try {
					ConsumerRecord<Object, Object> msg = queue.take();
					handleMsg(msg);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			logger.info(Thread.currentThread().getName() + " finished");
		}
	}


}
