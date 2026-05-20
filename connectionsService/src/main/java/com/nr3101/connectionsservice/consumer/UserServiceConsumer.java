package com.nr3101.connectionsservice.consumer;

import com.nr3101.connectionsservice.dto.request.PersonRequestDto;
import com.nr3101.connectionsservice.service.PersonService;
import com.nr3101.userservice.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceConsumer {

    private final PersonService personService;

    @KafkaListener(topics = "user_created_topic")
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for userId: {}", event.getUserId());

        PersonRequestDto personRequestDto = PersonRequestDto.builder()
                .userId(event.getUserId())
                .name(event.getName())
                .build();
        personService.createPerson(personRequestDto);

        log.info("Processed UserCreatedEvent for userId: {}", event.getUserId());
    }
}
