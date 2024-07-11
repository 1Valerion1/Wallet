package ru.cft.template.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.MoneyTransferCreateRequest;
import ru.cft.template.api.dto.MoneyTransferDto;
import ru.cft.template.api.dto.MoneyTransferFilteredTransfers;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.MoneyTransfer;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.InsufficientFundsException;
import ru.cft.template.core.exception.NotFoundException;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.MoneyTransferMapper;
import ru.cft.template.core.repository.MoneyTransferRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.MoneyTransferService;
import ru.cft.template.core.service.specification.MoneyTransferSpecifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.cft.template.core.exception.ErrorCode.OBJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private static final String MONEY_TRANSFER_NOT_FOUND = null;
    private final MoneyTransferRepository moneyTransferRepository;
    private final WalletRepository walletRepository;

    private final MoneyTransferMapper moneyTransferMapper;

    private User userSession() {
        return Context.get().getSessions().getUser();
    }

    private Wallet userWallet(Long id) {
        return walletRepository.findByUserId(id);
    }

    @Override
    public MoneyTransferDto create(MoneyTransferCreateRequest moneyTransferCreateRequest) {
        MoneyTransfer moneyTransfer = buildNewMoneyTransfer(moneyTransferCreateRequest);

        if (!walletRepository.existsByWalletId(Long.parseLong(moneyTransferCreateRequest.receiverWallet()))) {
            throw new NotFoundException("Wallet with id does not exist");
        }
        if (moneyTransferCreateRequest.amount() <= 0) {
            throw new InsufficientFundsException();
        }

        // Проверка баланса отправителя
        User sender = userSession();
        Wallet senderWallet = walletRepository.findByUserId(sender.getId());
        if (senderWallet.getBalance() < moneyTransferCreateRequest.amount()) {
            throw new InsufficientFundsException();
        }
        // Обновление баланса кошельков у получателя
        updateReceiverWallet(moneyTransferCreateRequest.receiverWallet(), moneyTransferCreateRequest.amount());
        // Обновление баланса кошельков у отправителя
        updateSenderWallet(sender, moneyTransferCreateRequest.amount());

        senderWallet.setBalance(senderWallet.getBalance() - moneyTransferCreateRequest.amount());
        walletRepository.update(senderWallet.getBalance(), sender.getId());


        // Сохранение данных о переводе
        moneyTransferRepository.save(moneyTransfer);

        return moneyTransferMapper.map(
                moneyTransfer,
                moneyTransferCreateRequest.receiverPhone(),
                Long.parseLong(moneyTransferCreateRequest.receiverWallet()));
    }
    private void updateReceiverWallet(String receiverWalletId, Integer transferAmount) {
        Wallet receiverWallet = walletRepository.findByUserId(Long.parseLong(receiverWalletId));
        receiverWallet.setBalance(receiverWallet.getBalance() + transferAmount);
        walletRepository.update(receiverWallet.getBalance(), Long.parseLong(receiverWalletId));
    }

    private void updateSenderWallet(User sender, Integer transferAmount) {
        Wallet senderWallet = walletRepository.findByUserId(sender.getId());
        senderWallet.setBalance(senderWallet.getBalance() - transferAmount);
        walletRepository.update(senderWallet.getBalance(), sender.getId());
    }
    @Override
    public MoneyTransfer buildNewMoneyTransfer(MoneyTransferCreateRequest dto) {


        return MoneyTransfer
                .builder()
                .amount(dto.amount())
                .user(Context.get().getSessions().getUser())
                .wallet(userWallet(Long.parseLong(dto.receiverWallet())))
                .comment(dto.comment())
                .status(Status.PAID)
                .transferId(UUID.randomUUID())
                .creatingTranslation(LocalDateTime.now())
                .build();
    }

    @Override
    public List<MoneyTransferDto> getByAllIdOrThrow(MoneyTransferFilteredTransfers filters) {
        Long userId = Context.get().getSessions().getUser().getId();

        List<MoneyTransfer> transfers;
        Specification<MoneyTransfer> specification = Specification.where(null);


        if (filters.receiverPhone() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withPhone(filters.receiverPhone()));
        }
        if (filters.receiverWallet() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withWalletId(Long.parseLong(filters.receiverWallet())));
        }
        if (filters.status() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withStatus(filters.status()));
        }
        if (filters.transferType() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withTransferType(filters.transferType()));
        }

        if (filters.receiverWallet() == null && filters.receiverPhone() == null) {
            specification = specification.or(MoneyTransferSpecifications.withUserId(userId));
        }

        transfers = moneyTransferRepository.findAll(specification);

        return moneyTransferMapper.mapList(
                transfers,
                userSession().getPhone(),
                userWallet(userSession().getId()).getWalletId());
    }

    @Override
    public MoneyTransferDto getById(UUID transferId) {
        System.out.println(transferId);
        return moneyTransferMapper.map(getByIdTransferOrThrow(transferId), userSession().getPhone(),
                userWallet(userSession().getId()).getWalletId());
    }

    private MoneyTransfer getByIdTransferOrThrow(UUID transferId) {
        return moneyTransferRepository.findById(transferId)
                .orElseThrow(() -> new ServiceException(OBJECT_NOT_FOUND, String.format(MONEY_TRANSFER_NOT_FOUND, transferId)));
    }
}
