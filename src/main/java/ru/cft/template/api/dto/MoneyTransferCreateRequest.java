package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import ru.cft.template.core.entity.Enum.TransferType;

@Builder
@Schema(description = "Information about MoneyTransferCreateRequest")
public record MoneyTransferCreateRequest(
        @Schema(example = "4555")
        Integer amount,
        @Enumerated(EnumType.STRING)
        TransferType transferType,
        @Schema(example = "79435352422")
        String receiverPhone,
        @Schema(example = "12")
        String receiverWallet,
        @Schema(example = "Что-то я устал")
        String comment) {
}
