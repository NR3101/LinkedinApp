package com.nr3101.connectionsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequestAcceptedEvent {

    private Long senderId;
    private Long receiverId;
}
