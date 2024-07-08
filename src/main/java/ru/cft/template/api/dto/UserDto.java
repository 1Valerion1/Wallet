package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
@Schema(description = "Information about UserDto")
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        int age,
        String phone,
        Instant registrationDate,
        Instant lastUpdateDate) {
}
