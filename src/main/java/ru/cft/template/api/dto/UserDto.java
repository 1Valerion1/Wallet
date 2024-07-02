package ru.cft.template.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        int age,
        long phone,
        LocalDateTime registrationDate,
        LocalDateTime lastUpdateDate) {
}
