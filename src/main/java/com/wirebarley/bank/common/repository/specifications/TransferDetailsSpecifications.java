package com.wirebarley.bank.common.repository.specifications;

import com.wirebarley.bank.common.entity.Trade;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TransferDetailsSpecifications {
    public static Specification<Trade> accountNumberEquals(String accountNumber) {
        return (root, query, criteriaBuilder) ->
                accountNumber == null ? null : criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
    }

    public static Specification<Trade> bankCodeEquals(String bankCode) {
        return (root, query, criteriaBuilder) ->
                bankCode == null ? null : criteriaBuilder.equal(root.get("bankCode"), bankCode);
    }

    public static Specification<Trade> tradeTypeEquals(String tradeType) {
        return (root, query, criteriaBuilder) ->
                tradeType == null ? null : criteriaBuilder.equal(root.get("tradeType"), tradeType);
    }

    public static Specification<Trade> tradeResultEquals(String tradeResult) {
        return (root, query, criteriaBuilder) ->
                tradeResult == null ? null : criteriaBuilder.equal(root.get("tradeResult"), tradeResult);
    }

    public static Specification<Trade> userIdEquals(String userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("registrationId"), userId);
    }

    public static Specification<Trade> tradeDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) ->
                (start == null || end == null) ? null : criteriaBuilder.between(root.get("registrationDate"), start, end);
    }
}
