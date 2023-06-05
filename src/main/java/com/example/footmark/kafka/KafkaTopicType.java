package com.example.footmark.kafka;

public enum KafkaTopicType {
	PRODUCER("producer"), CONSUMER_GROUP("consumer_group"), CONSUMER("consumer");
	private final String type;

	KafkaTopicType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
