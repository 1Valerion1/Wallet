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
import ru.cft.template.core.entity.Enum.TransferType;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "money_transfer" , schema = "wallet")
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransfer {
    @Id
    @Column(name = "transfer_id")
    @UuidGenerator
    private UUID transferId;

    private Integer amount;
    @Column
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private String receiverPhone;
    private String receiverWallet;

    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(name="creating_translation")
    @CreationTimestamp
    private Instant creatingTranslation;
}
