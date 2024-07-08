package ru.cft.template.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.PaymentInvoiceCreateRequest;
import ru.cft.template.api.dto.PaymentInvoiceDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.PaymentInvoice;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.InsufficientFundsException;
import ru.cft.template.core.exception.UserIdNotMatch;
import ru.cft.template.core.exception.UserNotFoundException;
import ru.cft.template.core.exception.UserPhoneException;
import ru.cft.template.core.mapper.PaymentInvoiceMapper;
import ru.cft.template.core.repository.PaymentInvoiceRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.PaymentInvoiceService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentInvoiceServiceImpl implements PaymentInvoiceService {

    private final PaymentInvoiceRepository paymentInvoiceRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final PaymentInvoiceMapper paymentInvoiceMapper;

    @Override
    public PaymentInvoiceDto create(PaymentInvoiceCreateRequest createRequest) {
        PaymentInvoice paymentInvoice = buildNewPayment(createRequest);
        paymentInvoiceRepository.save(paymentInvoice);

        return paymentInvoiceMapper.map(paymentInvoice);
    }

    private PaymentInvoice buildNewPayment(PaymentInvoiceCreateRequest createRequest) {
        User senderId = Context.get().getSession().getUser();
        return PaymentInvoice.builder()
                .amount(createRequest.amount())
                .senderId(senderId)
                .receiverId(createRequest.receiverId())
                .comment(createRequest.comment())
                .status(Status.UNPAID)
                .build();
    }

    @Override
    public PaymentInvoiceDto cancell(UUID invoiceId) {
        PaymentInvoice paymentInvoice = paymentInvoiceRepository.findByAccountNumber(invoiceId);
        if (!paymentInvoice.getSenderId()
                .getId()
                .equals(Context.get().getSession().getUser().getId())) {
            throw new UserIdNotMatch();

        }
        paymentInvoice.setStatus(Status.CANCELLED);
        paymentInvoiceRepository.updateStatusAndAccountNumber(invoiceId, paymentInvoice.getStatus());
        return paymentInvoiceMapper.map(paymentInvoice);
    }

    @Override
    public PaymentInvoiceDto paid(String phoneNumber) {
        Optional<User> userOptional = userRepository.findByPhone(phoneNumber);
        if (userOptional.isEmpty()) {
            throw new UserPhoneException();
        }
        if (!userOptional.get().getId().equals(Context.get().getSession().getUser().getId())) {
            throw new UserIdNotMatch();
        }
        User userPaingMoney = userOptional.get();
        Wallet walletPaingMoney = userPaingMoney.getWallet();

        // Ищем счета пользователя на оплаут
        List<PaymentInvoiceDto> paymentInvoiceList = getInfoPaymentUnpaid();

        PaymentInvoiceDto paidInvoice = null;
        for (PaymentInvoiceDto unpaidInvoice : paymentInvoiceList) {
            // Ищем счета пользователя на оплаут
            Optional<User> userReceivingMoney = userRepository.findById(unpaidInvoice.senderId().toString());
            if (userReceivingMoney.isEmpty()) {
                throw new UserNotFoundException();
            }

            Wallet walletReceivingMoney = userReceivingMoney.get().getWallet();
            // Проверяем, хватает ли средств для оплаты текущего счета
            if (unpaidInvoice.amount() <= walletPaingMoney.getBalance()) {
                // Если баланса хватает,то оплачиваем счет
                walletPaingMoney.setBalance(walletPaingMoney.getBalance() - unpaidInvoice.amount());
                walletReceivingMoney.setBalance(walletReceivingMoney.getBalance() + unpaidInvoice.amount());

                // Обновляем статус счета на "оплачен"
                paymentInvoiceRepository.updateStatusAndAccountNumber(unpaidInvoice.accountNumber(), Status.PAID);

                walletRepository.update(walletPaingMoney.getBalance(), userPaingMoney.getWallet().getWalletId());
                walletRepository.update(walletReceivingMoney.getBalance(), userReceivingMoney.get().getWallet().getWalletId());

                paidInvoice = new PaymentInvoiceDto(
                        unpaidInvoice.accountNumber(),
                        unpaidInvoice.amount(),
                        unpaidInvoice.senderId(),
                        unpaidInvoice.receiverId(),
                        Status.PAID,
                        unpaidInvoice.comment(),
                        unpaidInvoice.completedAt()
                );
                break;
            }
        }

        // Проверяем, был ли оплачен хотя бы один счет
        if (paidInvoice != null) {
            return paidInvoice;
        } else {
            throw new InsufficientFundsException();
        }
    }

    @Override
    public List<PaymentInvoiceDto> getInfoPaymentUnpaid() {
        Long reciverId = Context.get().getSession().getUser().getId();

        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findByReceiverIdAndStatus(reciverId,
                Status.UNPAID);
        return paymentInvoiceMapper.mapList(paymentInvoiceList);
    }

    @Override
    public PaymentInvoiceDto getInfoPaymentId(UUID paymentId) {
        PaymentInvoice paymentInvoice = paymentInvoiceRepository.findByAccountNumber(paymentId);

        return paymentInvoiceMapper.map(paymentInvoice);
    }

    @Override
    public List<PaymentInvoiceDto> getInfoPaymentAll() {
        User invoiceIssued = Context.get().getSession().getUser();
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findBySenderId(invoiceIssued);
        return paymentInvoiceMapper.mapList(paymentInvoiceList);
    }
}
