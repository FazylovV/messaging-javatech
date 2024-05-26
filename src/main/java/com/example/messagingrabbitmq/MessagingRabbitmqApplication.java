package com.example.messagingrabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class MessagingRabbitmqApplication {

	//static final String fanoutExchangeName = "spring-boot-exchange";

	private String queueName = "spring-boot" + UUID.randomUUID();

	@Bean
	protected Queue queue() {
		return new Queue(queueName, false, true, true);
	}

	@Bean
	protected FanoutExchange exchange() {
		return new FanoutExchange("spring-boot-exchange");
	}

	@Bean
	protected Binding binding(Queue queue, FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	protected SimpleMessageListenerContainer container(ConnectionFactory factory,
			MessageListenerAdapter adapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
		container.setQueueNames(queueName);
		container.setMessageListener(adapter);
		return container;
	}

	@Bean
	protected MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(MessagingRabbitmqApplication.class, args).close();
	}

}
