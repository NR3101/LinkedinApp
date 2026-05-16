package com.nr3101.connectionsservice.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
public class Person {

    @Id
    @GeneratedValue
    // Id for Neo4j, not to be confused with userId which is the ID from the User Service PostgreSQL DB
    private Long id;

    private Long userId;

    private String name;
}
