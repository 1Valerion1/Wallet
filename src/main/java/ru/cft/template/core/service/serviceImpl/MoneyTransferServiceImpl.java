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
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.MoneyTransferMapper;
import ru.cft.template.core.repository.MoneyTransferRepository;
import ru.cft.template.core.service.MoneyTransferService;
import ru.cft.template.core.service.specification.MoneyTransferSpecifications;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static ru.cft.template.core.Messages.MONEY_TRANSFER_NOT_FOUND;
import static ru.cft.template.core.exception.ErrorCode.OBJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;

    private final MoneyTransferMapper moneyTransferMapper;


    @Override
    public MoneyTransferDto create(MoneyTransferCreateRequest moneyTransferCreateRequest) {
        MoneyTransfer moneyTransfer = buildNewMoneyTransfer(moneyTransferCreateRequest);
        moneyTransferRepository.save(moneyTransfer);

        return moneyTransferMapper.map(moneyTransfer);
    }


    @Override
    public MoneyTransfer buildNewMoneyTransfer(MoneyTransferCreateRequest dto) {

        return MoneyTransfer
                .builder()
                .amount(dto.amount())
                .transferType(dto.transferType())
                .receiverPhone(dto.receiverPhone())
                .receiverWallet(dto.receiverWallet())
                .comment(dto.comment())
                .status(Status.UNPAID)
                .transferId(UUID.randomUUID())
                .creatingTranslation(Instant.now())
                .user(Context.get().getSession().getUser())
                .build();
    }

    @Override
    public List<MoneyTransferDto> getByAllIdOrThrow() {
        Long userId = Context.get().getSession().getUser().getId();
        return moneyTransferMapper.maplist(moneyTransferRepository.findByUserId(userId));
    }

    @Override
    public List<MoneyTransferDto> getByAllIdOrThrow(MoneyTransferFilteredTransfers filters) {
        Long userId = Context.get().getSession().getUser().getId();
        List<MoneyTransfer> transfers;
        Specification<MoneyTransfer> specification =
                Specification.where(MoneyTransferSpecifications.withUserId(userId));

        if (filters.receiverPhone() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withReceiverPhone(filters.receiverPhone()));
        }
        if (filters.receiverWallet() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withReceiverWallet(filters.receiverWallet()));
        }
        if (filters.status() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withStatus(filters.status()));
        }
        if (filters.transferType() != null) {
            specification = specification
                    .and(MoneyTransferSpecifications.withTransferType(filters.transferType()));
        }

        transfers = moneyTransferRepository.findAll(specification);

        return moneyTransferMapper.maplist(transfers);
    }

    @Override
    public MoneyTransferDto getById(UUID transferId) {
        System.out.println(transferId);
        return moneyTransferMapper.map(getByIdTransferOrThrow(transferId));
    }

    private MoneyTransfer getByIdTransferOrThrow(UUID transferId) {
        return moneyTransferRepository.findById(transferId)
                .orElseThrow(() -> new ServiceException(OBJECT_NOT_FOUND, String.format(MONEY_TRANSFER_NOT_FOUND, transferId)));
    }
}
