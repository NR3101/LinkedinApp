package com.nr3101.connectionsservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic connectionRequestSentTopic() {
        return new NewTopic("connection_request_sent_topic", 3, (short) 1);
    }

    @Bean
    public NewTopic connectionRequestAcceptedTopic() {
        return new NewTopic("connection_request_accepted_topic", 3, (short) 1);
    }
}
