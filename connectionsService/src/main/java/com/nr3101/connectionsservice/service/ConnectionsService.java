package com.nr3101.connectionsservice.service;

import com.nr3101.connectionsservice.entity.Person;

import java.util.List;

public interface ConnectionsService {

    List<Person> getFirstDegreeConnectionsOfUser(Long userId);
}
