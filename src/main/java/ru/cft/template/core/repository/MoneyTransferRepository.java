package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cft.template.core.entity.MoneyTransfer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, UUID>, JpaSpecificationExecutor<MoneyTransfer> {

    Optional<MoneyTransfer> findById(UUID transferId);

    List<MoneyTransfer> findByUserId(Long id);

}
