package com.nr3101.connectionsservice.service.impl;

import com.nr3101.connectionsservice.dto.request.PersonRequestDto;
import com.nr3101.connectionsservice.entity.Person;
import com.nr3101.connectionsservice.repository.PersonRepository;
import com.nr3101.connectionsservice.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createPerson(PersonRequestDto personRequestDto) {
        log.info("Creating person with name: {}", personRequestDto.getName());

        Person person = modelMapper.map(personRequestDto, Person.class);
        Person savedPerson = personRepository.save(person);

        log.info("Person created with ID: {}", savedPerson.getId());
    }
}
