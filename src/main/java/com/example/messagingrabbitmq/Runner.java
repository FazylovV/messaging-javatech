package com.example.messagingrabbitmq;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private Receiver receiver;

	@Override
	public void run(String... args) throws Exception {
		Scanner reader = new Scanner(System.in);
		while (true) {
			System.out.println("Send message:");
			String message = reader.nextLine();
			rabbitTemplate.convertAndSend("spring-boot-exchange", "foo.bar.baz", message);
			receiver.latch.await(10000, TimeUnit.MILLISECONDS);
		}
	}

}
