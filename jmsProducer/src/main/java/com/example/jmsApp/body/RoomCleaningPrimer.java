package com.example.jmsApp.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class RoomCleaningPrimer implements CommandLineRunner {

    @Value("${amqp.queue.name}")
    private String queuename;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomCleaningPrimer.class);


    private RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;
    private final ObjectMapper objectMapper;



    public RoomCleaningPrimer(RabbitTemplate template, ConfigurableApplicationContext context,ObjectMapper mapper)
    {
        super();
        this.restTemplate = new RestTemplate();
        this.rabbitTemplate = template;
        this.context = context;
        this.objectMapper =mapper;

    }
    @Override
    public void run(String... strings) throws Exception {
        String url = "http://localhost:8080/api/rooms";
        Room[] roomArray = this.restTemplate.getForObject(url, Room[].class);
        List<Room> rooms = Arrays.asList(roomArray);
        rooms.forEach(System.out::println);

        rooms.forEach(room -> {
            LOGGER.info("sending message");
            try {
                String jsonString = objectMapper.writeValueAsString(room);
                rabbitTemplate.convertAndSend(queuename,jsonString);

            }

            catch (JsonProcessingException e)
            {
                LOGGER.info("parsing exception",e);
            }


        });

        System.exit(SpringApplication.exit(context));
    }
}
