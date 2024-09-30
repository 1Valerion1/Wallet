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
import ru.cft.template.core.exception.NotFoundException;
import ru.cft.template.core.exception.UserIdNotMatch;
import ru.cft.template.core.mapper.PaymentInvoiceMapper;
import ru.cft.template.core.repository.PaymentInvoiceRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.PaymentInvoiceService;

import java.util.ArrayList;
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

    private Wallet userWallet(Long id) {
        return walletRepository.findByUserId(id);
    }

    private User getSenderId() {
        return Context.get().getUser();
    }

    @Override
    public PaymentInvoiceDto create(PaymentInvoiceCreateRequest createRequest) {
        PaymentInvoice paymentInvoice = buildNewPayment(createRequest);
       // paymentInvoice.setSenderId(getSenderId().getId());
        paymentInvoiceRepository.save(paymentInvoice);

        return paymentInvoiceMapper.map(paymentInvoice);
    }

    private PaymentInvoice buildNewPayment(PaymentInvoiceCreateRequest createRequest) {
        User senderId = getSenderId();
        return PaymentInvoice.builder()
                .amount(createRequest.amount())
                .senderId(senderId.getId())
                .receiverId(createRequest.receiverId())
                .comment(createRequest.comment())
                .status(Status.UNPAID)
                .build();
    }

    @Override
    public PaymentInvoiceDto cancell(UUID invoiceId) {
        PaymentInvoice paymentInvoice = paymentInvoiceRepository.findByAccountNumber(invoiceId);
        if (!paymentInvoice.getSenderId().equals(Context.get().getUser().getId())) {
            throw new UserIdNotMatch();

        }
        paymentInvoice.setStatus(Status.CANCELLED);
        paymentInvoiceRepository.updateStatusAndAccountNumber(invoiceId, paymentInvoice.getStatus());
        return paymentInvoiceMapper.map(paymentInvoice);
    }

    @Override
    public List<PaymentInvoiceDto> paid(String phoneNumber, UUID accountNumber) {
        User userPaingMoney = getUserByPhoneNumberAndValidate(phoneNumber);
        Wallet walletPayingMoney = userWallet(userPaingMoney.getId());
        // Ищем счета пользователя на оплату
        List<PaymentInvoiceDto> unpaidInvoices = getInfoPaymentAll(Status.UNPAID);

        if (accountNumber == null && unpaidInvoices.size() != 1) {
            return unpaidInvoices;
        } else {

            if (unpaidInvoices.size() == 1) {
                PaymentInvoiceDto invoiceToPay = getInfoPaymentId(unpaidInvoices.get(0).accountNumber());
                processPaymentIfUnpaidAndEnoughFunds(walletPayingMoney, invoiceToPay);
                return createPaidInvoice(invoiceToPay);
            }

            PaymentInvoiceDto invoiceToPay = getInfoPaymentId(accountNumber);
            processPaymentIfUnpaidAndEnoughFunds(walletPayingMoney, invoiceToPay);
            return createPaidInvoice(invoiceToPay);
        }
    }

    private User getUserByPhoneNumberAndValidate(String phoneNumber) {
        Optional<User> userOptional = userRepository.findByPhone(phoneNumber);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User no found!");
        }
        if (!userOptional.get().getId().equals(Context.get().getUser().getId())) {
            throw new UserIdNotMatch();
        }
        return userOptional.get();
    }

    private void processPaymentIfUnpaidAndEnoughFunds(Wallet walletPayingMoney, PaymentInvoiceDto invoiceToPay) {
        if (invoiceToPay.status() == Status.UNPAID) {
            if (invoiceToPay.amount() <= walletPayingMoney.getBalance()) {

                Optional<User> senderUser = userRepository.findById(String.valueOf(invoiceToPay.senderId()));

                Wallet walletSenderMoney = userWallet(senderUser.get().getId());
                processPayment(walletPayingMoney, walletSenderMoney, invoiceToPay);

            } else {
                throw new InsufficientFundsException();
            }
        } else {
            throw new RuntimeException();
        }
    }

    private void processPayment(Wallet walletPaingMoney, Wallet walletSenderMoney, PaymentInvoiceDto unpaidInvoice) {

        walletPaingMoney.setBalance(walletPaingMoney.getBalance() - unpaidInvoice.amount());
        walletSenderMoney.setBalance(walletSenderMoney.getBalance() + unpaidInvoice.amount());

        paymentInvoiceRepository.updateStatusAndAccountNumber(unpaidInvoice.accountNumber(), Status.PAID);

        walletRepository.update(walletPaingMoney.getBalance(), walletPaingMoney.getWalletId());
        walletRepository.update(walletSenderMoney.getBalance(), walletSenderMoney.getWalletId());
    }

    private List<PaymentInvoiceDto> createPaidInvoice(PaymentInvoiceDto unpaidInvoice) {
        List<PaymentInvoiceDto> paidInvoiceList = new ArrayList<>();
        PaymentInvoiceDto paidInvoice = new PaymentInvoiceDto(
                unpaidInvoice.accountNumber(),
                unpaidInvoice.amount(),
                unpaidInvoice.senderId(),
                unpaidInvoice.receiverId(),
                Status.PAID,
                unpaidInvoice.comment(),
                unpaidInvoice.completedAt()
        );
        paidInvoiceList.add(paidInvoice);
        return paidInvoiceList;
    }

    @Override
    public PaymentInvoiceDto getInfoPaymentId(UUID paymentId) {
        PaymentInvoice paymentInvoice = paymentInvoiceRepository.findByAccountNumber(paymentId);

        return paymentInvoiceMapper.map(paymentInvoice);
    }

    @Override
    public List<PaymentInvoiceDto> getInfoPaymentAll(Status status) {
        User invoiceIssued = Context.get().getUser();
        List<PaymentInvoice> paymentInvoiceList;

        if (status != null) {
            paymentInvoiceList =
                    paymentInvoiceRepository.findByReceiverIdAndStatus(invoiceIssued.getId(), status);
        } else {
            paymentInvoiceList = paymentInvoiceRepository.findBySenderId(invoiceIssued.getId());
        }

        return paymentInvoiceMapper.mapList(paymentInvoiceList);
    }
}
