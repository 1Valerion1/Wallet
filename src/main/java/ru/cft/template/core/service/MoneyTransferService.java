package ru.cft.template.core.service;

import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.MoneyTransferCreateRequest;
import ru.cft.template.api.dto.MoneyTransferDto;
import ru.cft.template.api.dto.MoneyTransferFilteredTransfers;
import ru.cft.template.core.entity.MoneyTransfer;

import java.util.List;
import java.util.UUID;

@Service
public interface MoneyTransferService {
    MoneyTransferDto create(MoneyTransferCreateRequest moneyTransferCreateRequest);

    MoneyTransfer buildNewMoneyTransfer(MoneyTransferCreateRequest dto);

    List<MoneyTransferDto> getByAllIdOrThrow(MoneyTransferFilteredTransfers moneyTransferFilteredTransfers);

    MoneyTransferDto getById(UUID transferId);


}
