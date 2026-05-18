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

    @Query("""
               match (p1:Person)-[r:REQUESTED_TO]->(p2:Person)
               where p1.userId = $fromUserId and p2.userId = $toUserId
               return count(r) > 0
            """)
    boolean connectionRequestExists(Long fromUserId, Long toUserId);

    @Query("""
               match (p1:Person)-[r:CONNECTED_TO]-(p2:Person)
               where p1.userId = $fromUserId and p2.userId = $toUserId
               return count(r) > 0
            """)
    boolean alreadyConnected(Long fromUserId, Long toUserId);

    @Query("""
               match (p1:Person), (p2:Person)
               where p1.userId = $fromUserId and p2.userId = $toUserId
               create (p1)-[:REQUESTED_TO]->(p2)
            """)
    void addConnectionRequest(Long fromUserId, Long toUserId);

    @Query("""
                match (p1:Person)-[r:REQUESTED_TO]->(p2:Person)
                where p1.userId = $fromUserId and p2.userId = $toUserId
                delete r
                create (p1)-[:CONNECTED_TO]->(p2)
            """)
    void acceptConnectionRequest(Long fromUserId, Long toUserId);

    @Query("""
                match (p1:Person)-[r:REQUESTED_TO]->(p2:Person)
                where p1.userId = $fromUserId and p2.userId = $toUserId
                delete r
            """)
    void rejectConnectionRequest(Long fromUserId, Long toUserId);
}
