package com.nr3101.connectionsservice.repository;

import com.nr3101.connectionsservice.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByUserId(Long userId);

    @Query("""
               match (p:Person)-[:CONNECTED_TO]-(friend:Person)
               where p.userId = $userId
               return friend
            """)
    List<Person> getFirstDegreeConnections(Long userId);
}
