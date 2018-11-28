package com.example.jmsApp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JmsAppApplication {


	@Value("${amqp.queue.name}")
	private String queuename;

	@Value("${amqp.exchange.name}")
	private String exchangePoint;


	@Bean
	public Queue queue()
	{
		return new Queue(queuename,false);
	}

	@Bean
	public TopicExchange topicExchange()
	{
		return new TopicExchange(exchangePoint);
	}

	@Bean
	public Binding binding(Queue queue,TopicExchange topicExchange)
	{

		return BindingBuilder.bind(queue).to(topicExchange).with(queuename);

	}

	public static void main(String[] args) {
		SpringApplication.run(JmsAppApplication.class, args);
	}
}
