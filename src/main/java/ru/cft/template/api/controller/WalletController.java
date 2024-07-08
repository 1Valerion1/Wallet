package ru.cft.template.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.HesoyamDto;
import ru.cft.template.api.dto.WalletDto;
import ru.cft.template.core.service.WalletService;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Операции над кошелько: получение информации о кошельке и рулетка(создается сразу при регистрации пользователя)")
public class WalletController {

    public final WalletService walletService;

    @GetMapping("/wallet")
    @Operation(description = "Возвращает информацию о кошельке пользователя.")
    public WalletDto getInfoWallet() {
        return walletService.getById();
    }

    @PatchMapping("/wallet/roulette")
    @Operation(description = "Рулетка с 25% шансом пользователь получает на счёт 10 д.е.")
    public HesoyamDto getRoulette() {
        return walletService.getRoulette();
    }

}
