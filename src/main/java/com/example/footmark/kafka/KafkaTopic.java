package com.example.footmark.kafka;

public enum KafkaTopic {

	HELLO("hello", "hello", "producer"),

	HELLO_CONSUMER("hello", "hello", "consumer_group");

	private final String topic;
	private final String key;
	private final String type;

	KafkaTopic(String topic, String key, String type) {
		this.topic = topic;
		this.key = key;
		this.type = type;
	}

	public String getTopic() {
		return topic;
	}

	public String getKey() {
		return key;
	}

	public String getType() {
		return type;
	}

}
