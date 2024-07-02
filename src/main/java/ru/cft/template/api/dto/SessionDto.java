package ru.cft.template.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SessionDto(
        LocalDateTime expirationTime,
        String value,
        boolean active) {
}
