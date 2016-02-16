package com.github.shazin.ms;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.shazin.ms.usereg.UserReceiver;
import com.shazin.github.ms.Config;

@SpringBootApplication
public class Main {	

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	Queue queue() {
		return new Queue(Config.USER_QUEUE_NAME);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(Config.USER_EXCHANGE_NAME);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(Config.USER_QUEUE_NAME);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Config.USER_QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}

    @Bean
    UserReceiver receiver() {
        return new UserReceiver();
    }

	@Bean
	MessageListenerAdapter listenerAdapter(UserReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Main.class, args);
    }

}
