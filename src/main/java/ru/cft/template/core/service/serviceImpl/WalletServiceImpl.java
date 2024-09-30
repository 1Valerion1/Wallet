package ru.cft.template.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.HesoyamDto;
import ru.cft.template.api.dto.WalletDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.mapper.WalletMapper;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.WalletService;

import java.security.SecureRandom;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final SecureRandom secureRandom;

    @Override
    public HesoyamDto getRoulette() {
        int chance = secureRandom.nextInt(100);
        if (chance < 25) {
            return updateBalanceIfWin(10, true);
        } else {
            return updateBalanceIfWin(0, false);
        }
    }

    private HesoyamDto updateBalanceIfWin(Integer amount, Boolean success) {
        Wallet wallet = getWalletByUserId();

        if (success) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.update(wallet.getBalance(), wallet.getWalletId());

            return resultRoulette(true, wallet.getBalance());
        }
        return resultRoulette(false, wallet.getBalance());
    }

    private HesoyamDto resultRoulette(Boolean success, Integer balance) {

        return new HesoyamDto(success, balance);
    }

    @Override
    public WalletDto getById() {
        Wallet wallet = getWalletByUserId();

        return walletMapper.map(wallet);
    }

    private Wallet getWalletByUserId() {
        Long userId = Context.get().getUser().getId();
        return walletRepository.findByUserId(userId);
    }
}
