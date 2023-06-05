package com.example.footmark.kafka;

import com.example.footmark.util.ThreadUtils;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

//@Component
public class KafkaProducerClient extends KafkaClientBase {
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerClient.class);

	private Producer<Object, Object> producer;
	// 使用一个阻塞队列来保存ProducerRecord
	private LinkedBlockingQueue<ProducerRecord<Object, Object>> queue;
	
	private volatile boolean exitFlag = false;

	public KafkaProducerClient() {
		//
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void init(Properties props) {
		producer = new KafkaProducer(props);
	}

	public void start() {
		queue = new LinkedBlockingQueue<>(1024);
		handleExecutor = Executors.newSingleThreadExecutor(ThreadUtils.createThreadFactory("kafka-producer-thread"));
		handleExecutor.submit(new KafkaProducerTask(producer));
	}

	public void shutdown() {
		exitFlag = true;
		super.stopExecutor();
		if (producer != null) {
			producer.close();
		}
	}

	public void sendMsg(final ProducerRecord<Object, Object> msg) {
		try {
			queue.put(msg);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	class KafkaProducerTask implements Runnable {
		private Producer<Object, Object> producer;

		public KafkaProducerTask(Producer<Object, Object> producer) {
			this.producer = producer;
		}

		private Future<RecordMetadata> sendMsg(final ProducerRecord<Object, Object> msg) {
			Future<RecordMetadata> future = producer.send(msg, new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					String logData = msg.toString();
					if (exception != null) {
						logger.error("send msg [" + logData + "] to kafka fail", exception);
						return;
					}
					logger.info("send msg [" + logData + "] to kafka success, offest is [" + metadata.offset() + "]");
				}
			});
			producer.flush();
			return future;
		}

		@Override
		public void run() {
			logger.info(Thread.currentThread().getName() + " started");
			while (!exitFlag) {
				ProducerRecord<Object, Object> msg;
				try {
					msg = queue.take();
					sendMsg(msg);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			logger.info(Thread.currentThread().getName() + " finished");
		}
	}
}
