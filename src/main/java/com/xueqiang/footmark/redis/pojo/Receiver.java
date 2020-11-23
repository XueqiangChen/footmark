package com.xueqiang.footmark.redis.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * n any messaging-based application, there are message publishers and messaging receivers.
 * To create the message receiver, implement a receiver with a method to respond to messages
 * The Receiver is a POJO that defines a method for receiving messages. When you register
 * the Receiver as a message listener, you can name the message-handling method whatever you want.
 */
public class Receiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	private AtomicInteger counter = new AtomicInteger();

	public void receiveMessage(String message) {
		LOGGER.info("Received <" + message + ">");
		counter.incrementAndGet();
	}

	public int getCount() {
		return counter.get();
	}
}
