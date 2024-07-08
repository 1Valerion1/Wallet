package ru.cft.template.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "wallet", schema = "wallet")
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @Column(name = "wallet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    private Integer balance;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany
    @Column
    @ToString.Exclude
    private Set<MoneyTransfer> moneyTransfers;
}
