package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(description = "Information about MoneyTransferCreateRequest")
public record MoneyTransferCreateRequest(
        @NotNull
        @Schema(example = "4555")
        Integer amount,

        @NotNull
        @Pattern(regexp = "^7(\\d{10})$")
        @Schema(example = "79435352422")
        String receiverPhone,

        @NotNull
        @Schema(example = "12")
        String receiverWallet,
        @NotNull
        @Pattern(regexp = "^.{0,49}")
        @Schema(example = "Тут я очень рад покупке")
        String comment) {
}
