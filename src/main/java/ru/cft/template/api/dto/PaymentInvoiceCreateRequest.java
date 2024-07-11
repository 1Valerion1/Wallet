package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder
@Schema(description = "Information about PaymentInvoiceCreateRequest")
public record PaymentInvoiceCreateRequest(
        @NotNull
        Integer amount,
        @NotNull
        Long receiverId,
        @Pattern(regexp = "^.{0,49}")
        String comment
) {
}
