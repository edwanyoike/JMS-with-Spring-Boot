package com.example.theConsumer;

import com.example.theConsumer.body.MessageProcessorClass;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter.*;


@SpringBootApplication
public class TheConsumerApplication {

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

	@Bean
	public MessageListenerAdapter listenerAdapter(MessageProcessorClass processorClass)
	{
		return new MessageListenerAdapter(processorClass,"receiveMessage");
	}

	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queuename);
		container.setMessageListener(listenerAdapter);

		return container;

	}

	public static void main(String[] args) {
		SpringApplication.run(TheConsumerApplication.class, args);
	}
}
