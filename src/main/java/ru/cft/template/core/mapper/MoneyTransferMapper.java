package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import ru.cft.template.api.dto.MoneyTransferDto;
import ru.cft.template.core.entity.MoneyTransfer;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface MoneyTransferMapper {

    default MoneyTransferDto map(MoneyTransfer moneyTransfer, String phone, Long walletId) {
        return MoneyTransferDto.builder()
                .transferId(moneyTransfer.getTransferId())
                .amount(moneyTransfer.getAmount())
                .transferType(moneyTransfer.getTransferType())
                .status(moneyTransfer.getStatus().toString())
                .receiverPhone(phone)
                .receiverWallet(walletId)
                .comment(moneyTransfer.getComment())
                .creatingTranslation(moneyTransfer.getCreatingTranslation())
                .build();
    }
    default List<MoneyTransferDto> mapList(List<MoneyTransfer> moneyTransfers, String phone, Long walletId) {
        return moneyTransfers.stream()
                .map(moneyTransfer -> map(moneyTransfer, phone, walletId))
                .collect(Collectors.toList());
    }
}
