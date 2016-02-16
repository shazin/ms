package com.github.shazin.ms.usereg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shazin.github.ms.Config;
import com.shazin.github.ms.user.User;

public class UserReceiver {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public void receiveMessage(String message) throws JsonMappingException, IOException {
		System.out.println("Received User : "+message);
		
		User user = mapper.readValue(message, User.class);
		
		System.out.println("User First Name : "+user.getFirstName());
		
		Map<String, String> accountProperties = new HashMap<String, String>();
		accountProperties.put("uniqueId", UUID.randomUUID().toString());
		accountProperties.put("firstName", user.getFirstName());
		accountProperties.put("lastName", user.getLastName());
		
		rabbitTemplate.convertAndSend(Config.ACCOUNT_QUEUE_NAME, mapper.writeValueAsString(accountProperties));
	}

}
