package ru.cft.template.api.dto;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.Enum.TransferType;


@Builder
@Schema(description = "Information about MoneyTransferFilteredTransfers")
public record MoneyTransferFilteredTransfers(

        @Enumerated(EnumType.STRING)
        @Nullable
        TransferType transferType,
        @Enumerated(EnumType.STRING)
        @Nullable
        Status status,
        @Nullable
        @Pattern(regexp = "^7(\\d{10})$")
        String receiverPhone,
        @Nullable
        String receiverWallet
        ) {
}
