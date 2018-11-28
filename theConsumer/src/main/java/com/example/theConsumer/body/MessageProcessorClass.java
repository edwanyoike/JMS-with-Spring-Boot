package com.example.theConsumer.body;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageProcessorClass {

    private final ObjectMapper objectMapper;

    @Autowired

    public MessageProcessorClass(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorClass.class);

    public void receiveMessage(String roomJson)
    {
        LOGGER.info("room received");

        try{
            Room room = this.objectMapper.readValue(roomJson,Room.class);
            LOGGER.info("room Message received for room : "+room.getNumber());

        }

        catch (IOException e)
        {
            LOGGER.error("exception : "+e.getMessage());
        }
    }

}
