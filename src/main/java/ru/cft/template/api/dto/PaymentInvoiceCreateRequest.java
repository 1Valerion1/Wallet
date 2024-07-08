package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
@Schema(description = "Information about PaymentInvoiceCreateRequest")
public record PaymentInvoiceCreateRequest(
        Integer amount,
        Long receiverId,
        String comment
) {
}
