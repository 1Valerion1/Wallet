package ru.cft.template.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.MoneyTransferCreateRequest;
import ru.cft.template.api.dto.MoneyTransferDto;
import ru.cft.template.api.dto.MoneyTransferFilteredTransfers;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.Enum.TransferType;
import ru.cft.template.core.service.MoneyTransferService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "MoneyTransfer", description = "Операции над денежным переводом: регистрация,обновление и получения данных о пользователе")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @PostMapping("/moneyTransfer")
    @Operation(description = "Создание и выполнение денежного перевода")
    public MoneyTransferDto create(@Valid @RequestBody MoneyTransferCreateRequest moneyTransferCreateRequest) {
        return moneyTransferService.create(moneyTransferCreateRequest);

    }

    @GetMapping("/moneyTransfer")
    @Operation(description = "Возвращает историю всех денежных переводов с возможностью фильтрации")
    public List<MoneyTransferDto> getHistory(@RequestParam(required = false) TransferType transferType,
                                             @RequestParam(required = false) Status status,
                                             @Valid @RequestParam(required = false) String receiverPhone,
                                             @RequestParam(required = false) String receiverWallet) {

        MoneyTransferFilteredTransfers filter = new MoneyTransferFilteredTransfers(transferType, status,
                receiverPhone, receiverWallet);

        return moneyTransferService.getByAllIdOrThrow(filter);
    }


    @GetMapping("/moneyTransfer/{transferId}")
    @Operation(description = "Возвращает информацию о конкретном денежном переводе")
    public MoneyTransferDto getInfoTransfer(@PathVariable UUID transferId) {
        return moneyTransferService.getById(transferId);
    }

}
