package ru.cft.template.core.service;

import ru.cft.template.api.dto.PaymentInvoiceCreateRequest;
import ru.cft.template.api.dto.PaymentInvoiceDto;
import ru.cft.template.core.entity.Enum.Status;

import java.util.List;
import java.util.UUID;

public interface PaymentInvoiceService {
    PaymentInvoiceDto create(PaymentInvoiceCreateRequest createRequest);

    PaymentInvoiceDto cancell(UUID invoiceId);

    List<PaymentInvoiceDto> paid(String phoneNumber, UUID accountNumber);

    PaymentInvoiceDto getInfoPaymentId(UUID paymentId);

    List<PaymentInvoiceDto> getInfoPaymentAll(Status status);
}
