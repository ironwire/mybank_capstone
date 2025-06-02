package com.mybank.myaccounts.repository;

import com.mybank.common.entity.Account;
import com.mybank.common.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);

    List<Transaction> findByAccountAccountId(Long accountId);

    List<Transaction> findByTransactionType(String transactionType);

    List<Transaction> findByTransactionStatus(String transactionStatus);

    Page<Transaction> findByAccountAccountNumber(String accountNumber, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByAccountAndDateRange(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.account.customer.customerId = :customerId")
    List<Transaction> findByCustomerId(@Param("customerId") Long customerId);
}