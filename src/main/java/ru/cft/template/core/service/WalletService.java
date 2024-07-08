package ru.cft.template.core.service;

import ru.cft.template.api.dto.HesoyamDto;
import ru.cft.template.api.dto.WalletDto;

public interface WalletService {

    HesoyamDto getRoulette();

    WalletDto getById();
}
