package ru.cft.template.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cft.template.api.dto.HesoyamDto;
import ru.cft.template.api.dto.WalletDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.mapper.WalletMapper;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.serviceImpl.WalletServiceImpl;

import java.security.SecureRandom;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @InjectMocks
    private WalletServiceImpl walletService;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private SecureRandom secureRandom;
    @Mock
    private WalletMapper walletMapper;

    @BeforeEach
    public void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);

        Context.get().setUser(mockUser);
    }


    @Test
    @DisplayName("Тест успешного использования рулетки")
    public void getRouletteSuccess() {
        when(secureRandom.nextInt(100)).thenReturn(24);

        Wallet wallet = new Wallet();
        wallet.setBalance(100);
        wallet.setWalletId(1l);

        when(walletRepository.findByUserId(1l)).thenReturn(wallet);

        HesoyamDto result = walletService.getRoulette();

        Assertions.assertTrue(result.success());
        Assertions.assertEquals(110, result.newBalance());

        verify(walletRepository, times(1)).update(110, 1L);
        verify(secureRandom, times(1)).nextInt(100);

    }

    @Test
    @DisplayName("Тест провала при использовании рулетки")
    public void getRouletteFail() {
        when(secureRandom.nextInt(100)).thenReturn(25);

        Wallet wallet = new Wallet();
        wallet.setBalance(100);
        wallet.setWalletId(1l);

        when(walletRepository.findByUserId(1l)).thenReturn(wallet);

        HesoyamDto result = walletService.getRoulette();

        Assertions.assertFalse(result.success());
        Assertions.assertEquals(100, result.newBalance());

        verify(walletRepository, never()).update(10, 1l);
    }

    @Test
    @DisplayName(value = "Проверка получения кошелька по ID, когда кошелек есть")
    public void getByIdSuccess() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100);
        wallet.setWalletId(1l);

        WalletDto walletDto = new WalletDto(1l,100);

        when(walletRepository.findByUserId(1l)).thenReturn(wallet);
        when(walletMapper.map(wallet)).thenReturn(walletDto);

        WalletDto walletDtoFind = walletService.getById();

        Assertions.assertEquals(walletDtoFind,walletDto);

        verify(walletRepository,times(1)).findByUserId(1l);
        verify(walletMapper,times(1)).map(wallet);

    }

    @Test
    @DisplayName(value = "Проверка получения кошелька по ID при отсутствии кошелька")
    public void getByIdFail() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100);
        wallet.setWalletId(1l);

        when(walletRepository.findByUserId(1l)).thenReturn(null);

        WalletDto walletDtoFind = walletService.getById();

        Assertions.assertNull(walletDtoFind);

        verify(walletRepository,times(1)).findByUserId(1l);
        verify(walletMapper,never()).map(wallet);
    }


}
