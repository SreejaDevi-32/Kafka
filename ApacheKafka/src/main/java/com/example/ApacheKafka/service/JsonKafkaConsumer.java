package com.example.ApacheKafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.ApacheKafka.model.User;

@Service
public class JsonKafkaConsumer {
	
private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	
	@KafkaListener(topics="javaguides_Json" , groupId = "myGroup")
	public void consume(User message)
	{
		logger.info(String.format("Message Received of User -> %s ", message.toString()));
		
	}

}
