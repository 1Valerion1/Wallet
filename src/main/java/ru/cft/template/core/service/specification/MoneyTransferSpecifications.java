package ru.cft.template.core.service.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.Enum.TransferType;
import ru.cft.template.core.entity.MoneyTransfer;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;

@Component
public class MoneyTransferSpecifications {
    public static Specification<MoneyTransfer> withUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<MoneyTransfer, User> wallet = root.join("user");
            return criteriaBuilder.equal(wallet.get("id"), userId);
        };
    }
    public static Specification<MoneyTransfer> withWalletId(Long walletId) {
        return (root, query, criteriaBuilder) -> {
            Join<MoneyTransfer, Wallet> wallet = root.join("wallet");
            return criteriaBuilder.equal(wallet.get("walletId"), walletId);
        };
    }


    public static Specification<MoneyTransfer> withPhone(String receiverPhone) {
        return (root, query, criteriaBuilder) -> {
            Join<MoneyTransfer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("phone"), receiverPhone);
        };
    }

    public static Specification<MoneyTransfer> withTransferType(TransferType transferType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("transferType"), transferType);
    }

    public static Specification<MoneyTransfer> withStatus(Status status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

}
