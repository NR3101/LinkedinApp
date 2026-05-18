package com.nr3101.connectionsservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDto {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotNull(message = "name cannot be null")
    private String name;
}
