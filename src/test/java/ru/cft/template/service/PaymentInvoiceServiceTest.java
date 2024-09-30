package ru.cft.template.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cft.template.api.dto.PaymentInvoiceCreateRequest;
import ru.cft.template.api.dto.PaymentInvoiceDto;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.PaymentInvoice;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.exception.UserIdNotMatch;
import ru.cft.template.core.mapper.PaymentInvoiceMapper;
import ru.cft.template.core.repository.PaymentInvoiceRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.serviceImpl.PaymentInvoiceServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentInvoiceServiceTest {
    @InjectMocks
    private PaymentInvoiceServiceImpl paymentInvoiceService;
    @Mock
    private PaymentInvoiceRepository paymentInvoiceRepository;
    @Mock
    private PaymentInvoiceMapper paymentInvoiceMapper;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);

        Context.get().setUser(mockUser);
    }

    @Test
    @DisplayName("Позитивный сценарий создания платежного документа")
    public void createSuccess() {
        PaymentInvoiceCreateRequest paymentRequest = PaymentInvoiceCreateRequest.builder()
                .amount(1000)
                .receiverId(12l)
                .comment("Все понравилось")
                .build();

        PaymentInvoice paymentInvoice = PaymentInvoice.builder()
                .amount(paymentRequest.amount())
                .senderId(1l)
                .receiverId(paymentRequest.receiverId())
                .comment(paymentRequest.comment())
                .status(Status.UNPAID)
                .build();

        PaymentInvoiceDto paymentInvoiceDto =  PaymentInvoiceDto.builder()
                .accountNumber(UUID.randomUUID())
                .amount(paymentRequest.amount())
                .senderId(1l)
                .receiverId(paymentRequest.receiverId())
                .comment(paymentRequest.comment())
                .status(Status.UNPAID)
                .completedAt(LocalDateTime.now())
                .build();


        when(paymentInvoiceMapper.map(paymentInvoice)).thenReturn(paymentInvoiceDto);
        when(paymentInvoiceRepository.save(paymentInvoice)).thenReturn(paymentInvoice);

        PaymentInvoiceDto resultDto = paymentInvoiceService.create(paymentRequest);

        Assertions.assertEquals(paymentInvoiceDto, resultDto);

        verify(paymentInvoiceRepository, times(1)).save(paymentInvoice);

    }

    @Test
    @DisplayName("Позитивный сценарий отмены платежного документа")
    public void cancellSuccess() {
        UUID invoiceId = UUID.randomUUID();
        PaymentInvoice paymentInvoice = new PaymentInvoice();
        paymentInvoice.setSenderId(1L);
        paymentInvoice.setStatus(Status.UNPAID);

        PaymentInvoiceDto paymentInvoiceDto =  PaymentInvoiceDto.builder()
                .accountNumber(UUID.randomUUID())
                .amount(1000)
                .senderId(1l)
                .receiverId(12l)
                .comment("")
                .status(Status.CANCELLED)
                .completedAt(LocalDateTime.now())
                .build();

        when(paymentInvoiceRepository.findByAccountNumber(invoiceId)).thenReturn(paymentInvoice);

        when(paymentInvoiceMapper.map(paymentInvoice)).thenReturn(paymentInvoiceDto);

        PaymentInvoiceDto result = paymentInvoiceService.cancell(invoiceId);

        Assertions.assertEquals(Status.CANCELLED, paymentInvoice.getStatus());
        Assertions.assertEquals(paymentInvoiceDto, result);

        verify(paymentInvoiceRepository, times(1)).updateStatusAndAccountNumber(invoiceId, Status.CANCELLED);
    }

    @Test
    @DisplayName("Негативный сценарий: ID пользователя не совпадает при отмене счета")
    public void cancellUserIdMismatch() {
        UUID invoiceId = UUID.randomUUID();
        PaymentInvoice paymentInvoice = new PaymentInvoice();
        paymentInvoice.setSenderId(999L);

        when(paymentInvoiceRepository.findByAccountNumber(invoiceId)).thenReturn(paymentInvoice);

        assertThrows(UserIdNotMatch.class, () -> {
            paymentInvoiceService.cancell(invoiceId);
        });
    }

//    @Test
//    @DisplayName("Позитивный сценарий оплаты счета")
//    public void paidSuccess() {
//        String phoneNumber = "72345672890";
//        UUID accountNumber = UUID.randomUUID();
//
//        User senderUser = new User();
//        senderUser.setId(1L);
//        senderUser.setPhone(phoneNumber);
//
//        Wallet senderWallet = new Wallet();
//        senderWallet.setWalletId(1L);
//        senderWallet.setBalance(1500);
//
//        User receiverUser = new User();
//        receiverUser.setId(2L);
//
//        Wallet receiverWallet = new Wallet();
//        receiverWallet.setWalletId(2L);
//        receiverWallet.setBalance(2000);
//
//        PaymentInvoice paymentInvoice = new PaymentInvoice();
//        paymentInvoice.setAmount(1000);
//        paymentInvoice.setSenderId(1L);
//        paymentInvoice.setReceiverId(2L);
//        paymentInvoice.setAccountNumber(accountNumber);
//        paymentInvoice.setStatus(Status.UNPAID);
//
//        PaymentInvoiceDto paymentInvoiceDto = new PaymentInvoiceDto(
//                accountNumber,
//                1000,
//                1L,
//                2L,
//                Status.UNPAID,
//                "Some comment",
//                null
//        );
//
//        when(userRepository.findByPhone(phoneNumber)).thenReturn(Optional.of(senderUser));
//        when(userRepository.findById("1")).thenReturn(Optional.of(senderUser));
//        when(walletRepository.findByUserId(1L)).thenReturn(senderWallet);
//
//        when(userRepository.findById("2")).thenReturn(Optional.of(receiverUser));
//        when(walletRepository.findByUserId(2L)).thenReturn(receiverWallet);
//
//        when(paymentInvoiceRepository.findByAccountNumber(accountNumber)).thenReturn(paymentInvoice);
//        when(paymentInvoiceMapper.map(paymentInvoice)).thenReturn(paymentInvoiceDto);
//        doNothing().when(paymentInvoiceRepository).updateStatusAndAccountNumber(eq(accountNumber), eq(Status.PAID));
//
//        List<PaymentInvoiceDto> result = paymentInvoiceService.paid(phoneNumber, accountNumber);
//
//        assertEquals(1, result.size());
//        assertEquals(Status.PAID, result.get(0).status());
//
//        // Обновляем состояние объектов Wallet
//        senderWallet.setBalance(senderWallet.getBalance() - 1000);
//        receiverWallet.setBalance(receiverWallet.getBalance() + 1000);
//
//        Assertions.assertEquals(500, senderWallet.getBalance());
//        Assertions.assertEquals(3000, receiverWallet.getBalance());
//
//        verify(paymentInvoiceRepository, times(1)).updateStatusAndAccountNumber(accountNumber, Status.PAID);
//        verify(walletRepository, times(1)).update(senderWallet.getBalance(), senderWallet.getWalletId());
//        verify(walletRepository, times(1)).update(receiverWallet.getBalance(), receiverWallet.getWalletId());
//    }




}
