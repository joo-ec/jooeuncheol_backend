package com.wirebarley.bank.common.repository;

import com.wirebarley.bank.common.entity.Trade;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> , JpaSpecificationExecutor<Trade> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Trade> findByBankCodeAndAccountNumber(String bankCode, String accountNumber);

}
