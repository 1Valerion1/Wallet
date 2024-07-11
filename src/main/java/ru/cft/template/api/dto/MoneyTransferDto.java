package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Information about MoneyTransferDto")
public record MoneyTransferDto(
        UUID transferId,
        Integer amount,
        @Enumerated(EnumType.STRING)
        String status,
        String receiverPhone,
        Long receiverWallet,
        String comment,
        LocalDateTime creatingTranslation) {
}
