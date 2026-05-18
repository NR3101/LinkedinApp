package com.nr3101.connectionsservice.controller;

import com.nr3101.connectionsservice.entity.Person;
import com.nr3101.connectionsservice.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/{userId}/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnections(
            @PathVariable Long userId
    ) {
        log.info("Received request to get first-degree connections for user ID: {}", userId);
        List<Person> firstDegreeConnections = connectionsService.getFirstDegreeConnectionsOfUser(userId);
        return ResponseEntity.ok(firstDegreeConnections);
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Void> sendConnectionRequest(
            @PathVariable Long userId
    ) {
        log.info("Received request to send connection request for user ID: {}", userId);
        connectionsService.sendConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Void> acceptConnectionRequest(
            @PathVariable Long userId
    ) {
        log.info("Received request to accept connection request from user ID: {}", userId);
        connectionsService.acceptConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/reject/{userId}")
    public ResponseEntity<Void> rejectConnectionRequest(
            @PathVariable Long userId
    ) {
        log.info("Received request to reject connection request from user ID: {}", userId);
        connectionsService.rejectConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }
}
