package ru.cft.template.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.cft.template.api.dto.UserCreateRequest;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.ConflictException;
import ru.cft.template.core.mapper.UserMapper;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.serviceImpl.UserService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Тест удачного создания пользователя")
    public void testCreateSuccess() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .patronymic("Иванович")
                .phone("79435352422")
                .email("sdfsd@gmail.com")
                .birthday(LocalDate.of(2002, 12, 24))
                .password("Koroli1!")
                .build();

        when(passwordEncoder.encode(userCreateRequest.password())).thenReturn("codedPassword");
        when(userRepository.existsByEmail(userCreateRequest.email())).thenReturn(false);
        when(userRepository.existsByPhone(userCreateRequest.phone())).thenReturn(false);

        userService.create(userCreateRequest);

        verify(userRepository).existsByEmail(userCreateRequest.email());
        verify(userRepository).existsByPhone(userCreateRequest.phone());
        verify(passwordEncoder).encode(userCreateRequest.password());

        verify(walletRepository, times(1)).save(Mockito.any(Wallet.class));
    }


    @Test
    @DisplayName("Тест неудачного создания пользователя - email уже существует")
    public void testCreateFailByEmail() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .patronymic("Иванович")
                .phone("79435352422")
                .email("sdfsd@gmail.com")
                .birthday(LocalDate.of(2002,12,24))
                .password("Koroli1!")
                .build();

        when(userRepository.existsByEmail(userCreateRequest.email())).thenReturn(true);

        Assertions.assertThrows(
                ConflictException.class,
                () -> userService.create(userCreateRequest)
        );

       verify(userRepository,never()).save(any());
       verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Тест неудачного создания пользователя - phone уже существует")
    public void testCreateFailByPhone() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .patronymic("Иванович")
                .phone("79435352422")
                .email("sdfsd@gmail.com")
                .birthday(LocalDate.of(2002,12,24))
                .password("Koroli1!")
                .build();

        when(userRepository.existsByPhone(userCreateRequest.phone())).thenReturn(true);

        Assertions.assertThrows(
                ConflictException.class,
                () -> userService.create(userCreateRequest)
        );

        verify(userRepository,never()).save(any());
        verify(userRepository, never()).save(any());
    }


}
