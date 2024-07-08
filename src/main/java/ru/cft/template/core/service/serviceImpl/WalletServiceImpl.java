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


    @Override
    public HesoyamDto getRoulette() {
        SecureRandom secureRandom = new SecureRandom();
        int chance = secureRandom.nextInt(100);

        if (chance < 25) {
            return updateBalance(10, true);
        } else {
            return updateBalance(0, false);
        }
    }

    public HesoyamDto updateBalance(Integer amount, Boolean trueFalse) {
        Long userId = Context.get().getSession().getUser().getId();
        Wallet wallet = walletRepository.findByUserId(userId);
        // throw new UsernameNotFoundException(String.format("Wallet with userId %s not found", userId));
        if (trueFalse) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.update(wallet.getBalance(), wallet.getWalletId());

            return new HesoyamDto(true, wallet.getBalance());
        }
        return new HesoyamDto(false, wallet.getBalance());
    }

    @Override
    public WalletDto getById() {
        Long userId = Context.get().getSession().getUser().getId();
        Wallet wallet = walletRepository.findByUserId(userId);

        return walletMapper.map(wallet);
    }
}
