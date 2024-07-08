package ru.cft.template.core.service.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.cft.template.core.entity.Enum.Status;
import ru.cft.template.core.entity.Enum.TransferType;
import ru.cft.template.core.entity.MoneyTransfer;
import ru.cft.template.core.entity.User;

@Component
public class MoneyTransferSpecifications {

    public static Specification<MoneyTransfer> withUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<MoneyTransfer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("Id"), userId);
        };
    }

    public static Specification<MoneyTransfer> withReceiverPhone(String receiverPhone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receiverPhone"), receiverPhone);
    }

    public static Specification<MoneyTransfer> withReceiverWallet(String receiverWallet) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("receiverWallet"), receiverWallet);
    }
    public static Specification<MoneyTransfer> withTransferType(TransferType transferType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("transferType"), transferType);
    }

    public static Specification<MoneyTransfer> withStatus(Status status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

}
