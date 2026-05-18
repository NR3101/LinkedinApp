package com.nr3101.connectionsservice.service.impl;

import com.nr3101.connectionsservice.auth.AuthContextHolder;
import com.nr3101.connectionsservice.entity.Person;
import com.nr3101.connectionsservice.event.ConnectionRequestAcceptedEvent;
import com.nr3101.connectionsservice.event.ConnectionRequestSentEvent;
import com.nr3101.connectionsservice.exception.ConflictException;
import com.nr3101.connectionsservice.repository.PersonRepository;
import com.nr3101.connectionsservice.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, ConnectionRequestSentEvent> requestSentEventKafkaTemplate;
    private final KafkaTemplate<Long, ConnectionRequestAcceptedEvent> requestAcceptedEventKafkaTemplate;

    @Override
    public List<Person> getFirstDegreeConnectionsOfUser(Long userId) {
        log.info("Getting first degree connections for user with ID: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    @Override
    public void sendConnectionRequest(Long toUserId) {
        log.info("Sending connection request for user with ID: {}", toUserId);

        Long fromUserId = AuthContextHolder.getCurrentUserId();

        if (fromUserId.equals(toUserId)) {
            log.warn("User with ID: {} cannot send connection request to themselves", fromUserId);
            throw new ConflictException("Cannot send connection request to yourself");
        }

        boolean requestExists = personRepository.connectionRequestExists(fromUserId, toUserId);
        if (requestExists) {
            log.warn("Connection request already exists from user ID: {} to user ID: {}", fromUserId, toUserId);
            throw new ConflictException("Connection request already exists");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(fromUserId, toUserId);
        if (alreadyConnected) {
            log.warn("Users with ID: {} and ID: {} are already connected", fromUserId, toUserId);
            throw new ConflictException("Users are already connected");
        }

        personRepository.addConnectionRequest(fromUserId, toUserId);
        log.info("Connection request sent from user ID: {} to user ID: {}", fromUserId, toUserId);

        // Publish event to Kafka
        ConnectionRequestSentEvent event = ConnectionRequestSentEvent.builder()
                .senderId(fromUserId)
                .receiverId(toUserId)
                .build();
        requestSentEventKafkaTemplate.send("connection_request_sent_topic", event);
        log.info("Published ConnectionRequestSentEvent to Kafka for sender ID: {} and receiver ID: {}", fromUserId, toUserId);
    }

    @Override
    public void acceptConnectionRequest(Long fromUserId) {
        log.info("Accepting connection request from user with ID: {}", fromUserId);

        Long toUserId = AuthContextHolder.getCurrentUserId();

        if (fromUserId.equals(toUserId)) {
            log.warn("User with ID: {} cannot accept connection request from themselves", toUserId);
            throw new ConflictException("Cannot accept connection request from yourself");
        }

        boolean requestExists = personRepository.connectionRequestExists(fromUserId, toUserId);
        if (!requestExists) {
            log.warn("No connection request exists from user ID: {} to user ID: {}", fromUserId, toUserId);
            throw new ConflictException("No connection request exists");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(fromUserId, toUserId);
        if (alreadyConnected) {
            log.warn("Users with ID: {} and ID: {} are already connected", fromUserId, toUserId);
            throw new ConflictException("Users are already connected");
        }

        personRepository.acceptConnectionRequest(fromUserId, toUserId);
        log.info("Connection request accepted from user ID: {} to user ID: {}", fromUserId, toUserId);

        // Publish event to Kafka
        ConnectionRequestAcceptedEvent event = ConnectionRequestAcceptedEvent.builder()
                .senderId(fromUserId)
                .receiverId(toUserId)
                .build();
        requestAcceptedEventKafkaTemplate.send("connection_request_accepted_topic", event);
        log.info("Published ConnectionRequestAcceptedEvent to Kafka for sender ID: {} and receiver ID: {}", fromUserId, toUserId);
    }

    @Override
    public void rejectConnectionRequest(Long fromUserId) {
        log.info("Rejecting connection request from user with ID: {}", fromUserId);

        Long toUserId = AuthContextHolder.getCurrentUserId();

        if (fromUserId.equals(toUserId)) {
            log.warn("User with ID: {} cannot reject connection request from themselves", toUserId);
            throw new ConflictException("Cannot reject connection request from yourself");
        }

        boolean requestExists = personRepository.connectionRequestExists(fromUserId, toUserId);
        if (!requestExists) {
            log.warn("No connection request exists from user ID: {} to user ID: {}", fromUserId, toUserId);
            throw new ConflictException("No connection request exists");
        }

        personRepository.rejectConnectionRequest(fromUserId, toUserId);
        log.info("Connection request rejected from user ID: {} to user ID: {}", fromUserId, toUserId);
    }
}
