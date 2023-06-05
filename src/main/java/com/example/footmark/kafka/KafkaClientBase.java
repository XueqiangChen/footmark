package com.example.footmark.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class KafkaClientBase {

	private static final Logger logger = LoggerFactory.getLogger(KafkaClientBase.class);

	protected ExecutorService handleExecutor;

	protected void stopExecutor() {
		if (!handleExecutor.isShutdown()) {
			handleExecutor.shutdown();
			try {
				handleExecutor.awaitTermination(3, TimeUnit.SECONDS);
			} catch (Exception e) {
				logger.info("{}: interrupted when waiting terminating.", "kafka executor", e);
			} finally {
				handleExecutor.shutdownNow();
			}
		}
	}
}
