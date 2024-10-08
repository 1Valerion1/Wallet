package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.core.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Wallet findByUserId(Long userid);

    boolean existsByWalletId(Long walletId);

    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance = :balance where w.walletId = :walletId")
    void update(@Param("balance") Integer balance, @Param("walletId") Long walletId);


}
