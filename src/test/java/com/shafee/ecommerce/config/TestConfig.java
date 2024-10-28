package com.shafee.ecommerce.config;

import com.shafee.ecommerce.messaging.MessageQueueService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.mockito.Mockito;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public MessageQueueService testMessageQueueService() {
        return Mockito.mock(MessageQueueService.class);
    }
}