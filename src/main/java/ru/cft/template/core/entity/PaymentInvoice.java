package ru.cft.template.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.cft.template.core.entity.Enum.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "payment_invoice", schema = "wallet")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInvoice {
    @Id
    @UuidGenerator
    private UUID accountNumber;

    private Integer amount;

    private Long receiverId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    @ToString.Exclude
    private User senderId;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    private String comment;

    @CreationTimestamp
    private LocalDateTime completedAt;

}
