package com.github.shazin.ms;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.shazin.github.ms.Config;

@EnableAutoConfiguration
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	org.springframework.amqp.core.Queue userQueue() {
		return new org.springframework.amqp.core.Queue(Config.USER_QUEUE_NAME);
	}

	@Bean
	TopicExchange userExchange() {
		return new TopicExchange(Config.USER_EXCHANGE_NAME);
	}

	@Bean
	org.springframework.amqp.core.Binding userBinding(
			org.springframework.amqp.core.Queue userQueue,
			TopicExchange userExchange) {
		return BindingBuilder.bind(userQueue).to(userExchange)
				.with(Config.USER_QUEUE_NAME);
	}

	@Bean
	org.springframework.amqp.core.Queue accountQueue() {
		return new org.springframework.amqp.core.Queue(
				Config.ACCOUNT_QUEUE_NAME);
	}

	@Bean
	TopicExchange accountExchange() {
		return new TopicExchange(Config.ACCOUNT_EXCHANGE_NAME);
	}

	@Bean
	org.springframework.amqp.core.Binding binding(
			org.springframework.amqp.core.Queue accountQueue,
			TopicExchange accountExchange) {
		return BindingBuilder.bind(accountQueue).to(accountExchange)
				.with(Config.ACCOUNT_QUEUE_NAME);
	}

}
