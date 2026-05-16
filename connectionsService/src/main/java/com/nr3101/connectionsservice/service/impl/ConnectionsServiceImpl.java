package com.nr3101.connectionsservice.service.impl;

import com.nr3101.connectionsservice.entity.Person;
import com.nr3101.connectionsservice.repository.PersonRepository;
import com.nr3101.connectionsservice.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getFirstDegreeConnectionsOfUser(Long userId) {
        log.info("Getting first degree connections for user with ID: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }
}
