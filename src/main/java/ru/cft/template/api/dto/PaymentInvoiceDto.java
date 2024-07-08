package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import ru.cft.template.core.entity.Enum.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Information about PaymentInvoiceDto")
public record PaymentInvoiceDto(

        UUID accountNumber,
        Integer amount,
        Long senderId,
        Long receiverId,
        @Enumerated(EnumType.STRING)
        Status status,
        @Size(max = 250)
        String comment,
        @CreationTimestamp
        LocalDateTime completedAt

) {
}
