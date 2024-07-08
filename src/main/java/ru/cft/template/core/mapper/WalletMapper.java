package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import ru.cft.template.api.dto.WalletDto;
import ru.cft.template.core.entity.Wallet;

@Mapper
public interface WalletMapper {

    WalletDto map(Wallet wallet);

}
