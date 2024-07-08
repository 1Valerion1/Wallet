package ru.cft.template.core.service;

import ru.cft.template.api.dto.PaymentInvoiceCreateRequest;
import ru.cft.template.api.dto.PaymentInvoiceDto;

import java.util.List;
import java.util.UUID;

public interface PaymentInvoiceService {
    PaymentInvoiceDto create(PaymentInvoiceCreateRequest createRequest);

    PaymentInvoiceDto cancell(UUID invoiceId);

    PaymentInvoiceDto paid(String phoneNumber);

    List<PaymentInvoiceDto> getInfoPaymentUnpaid();

    PaymentInvoiceDto getInfoPaymentId(UUID paymentId);

    List<PaymentInvoiceDto> getInfoPaymentAll();
}
