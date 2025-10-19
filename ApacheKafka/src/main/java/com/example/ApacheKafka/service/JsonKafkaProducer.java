package com.example.ApacheKafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.ApacheKafka.model.User;

@Service
public class JsonKafkaProducer {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonKafkaProducer.class);
	
	private KafkaTemplate<String,User> kafkaTemplate;

	public JsonKafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
		super();
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendMessage(User data) {
		
		logger.info(String.format("Message sent -> %s", data.toString()));
		
		Message<User> message = MessageBuilder.withPayload(data)
											.setHeader(KafkaHeaders.TOPIC, "javaguides_Json")
											.build();
		
		kafkaTemplate.send(message);
	}
	
	

}
