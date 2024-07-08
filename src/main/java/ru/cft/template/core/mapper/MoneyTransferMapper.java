package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cft.template.api.dto.MoneyTransferDto;
import ru.cft.template.core.entity.MoneyTransfer;

import java.util.List;

@Mapper
public interface MoneyTransferMapper {
    @Mapping(target = "creating_translation", source = "creatingTranslation")
    MoneyTransferDto map(MoneyTransfer moneyTransfer);

    @Mapping(target = "creating_translation", source = "creatingTranslation")
    List<MoneyTransferDto> maplist(List<MoneyTransfer> moneyTransfer);

}
