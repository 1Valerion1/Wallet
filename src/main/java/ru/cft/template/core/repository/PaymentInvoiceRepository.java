package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.PaymentInvoice;
import ru.cft.template.core.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, String> {
    List<PaymentInvoice> findBySenderId(User id);

    PaymentInvoice findByAccountNumber(UUID id);

    List<PaymentInvoice> findByReceiverIdAndStatus(Long id, Status unpaid);

    @Modifying
    @Query("UPDATE PaymentInvoice pi SET pi.status = :status WHERE pi.accountNumber = :accountNumber")
    @Transactional
    void updateStatusAndAccountNumber(@Param("accountNumber") UUID accountNumber, @Param("status") Status status);
}
