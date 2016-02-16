package com.github.shazin.ms.user;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shazin.github.ms.Config;
import com.shazin.github.ms.user.User;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody @Valid User user) throws JsonProcessingException {
		String json = mapper.writeValueAsString(user);
		
		rabbitTemplate.convertAndSend(Config.USER_QUEUE_NAME, json);
	}
}
