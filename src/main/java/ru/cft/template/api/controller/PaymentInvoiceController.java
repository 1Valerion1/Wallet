package ru.cft.template.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.PaymentInvoiceCreateRequest;
import ru.cft.template.api.dto.PaymentInvoiceDto;
import ru.cft.template.core.service.PaymentInvoiceService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "PaymentInvoice", description = "Операции над cчёт на оплат")
public class PaymentInvoiceController {

    public final PaymentInvoiceService paymentInvoiceService;

    @PostMapping("/paymentInvoice")
    @Operation(description = "Создает и выставляет новый счет на оплату.")
    public PaymentInvoiceDto create(@Valid @RequestBody PaymentInvoiceCreateRequest paymentInvoiceCreateRequest) {
        return paymentInvoiceService.create(paymentInvoiceCreateRequest);
    }

    @GetMapping("/paymentInvoice")
    @Operation(description = "Получение списка всех выставленных счетов")
    public List<PaymentInvoiceDto> getInfoPaymentAll() {
        return paymentInvoiceService.getInfoPaymentAll();
    }

    @GetMapping("/paymentInvoice/unpaid")
    @Operation(description = "Возвращает список всех выставленных счетов к оплате.")
    public List<PaymentInvoiceDto> getInfoPayment() {
        return paymentInvoiceService.getInfoPaymentUnpaid();
    }

    @GetMapping("/paymentInvoice/{paymentId}")
    @Operation(description = "Возвращает информацию о конкретном счете на оплату.")
    public PaymentInvoiceDto getInfoPaymentId(@PathVariable UUID paymentId) {
        return paymentInvoiceService.getInfoPaymentId(paymentId);
    }

    @PatchMapping("/pay/{phoneNumber}")
    @Operation(description = "Оплачивает существующий счет на оплату.")
    public PaymentInvoiceDto paid(@Valid @PathVariable String phoneNumber) {
        return paymentInvoiceService.paid(phoneNumber);
    }

    @PatchMapping("/cancell/{invoiceId}")
    @Operation(description = "Отменяет существующий счет на оплату.")
    public PaymentInvoiceDto cancell(@PathVariable UUID invoiceId) {
        return paymentInvoiceService.cancell(invoiceId);
    }
}
