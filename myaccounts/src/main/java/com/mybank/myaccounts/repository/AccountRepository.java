package com.mybank.myaccounts.repository;

import com.mybank.common.entity.Account;
import com.mybank.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomer(Customer customer);

    List<Account> findByCustomerCustomerId(Long customerId);

    List<Account> findByStatus(String status);

    @Query("SELECT a FROM Account a WHERE a.balance > :minimumBalance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("minimumBalance") BigDecimal minimumBalance);

    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId AND a.accountType.name = :accountTypeName")
    List<Account> findByCustomerIdAndAccountTypeName(@Param("customerId") Long customerId, @Param("accountTypeName") String accountTypeName);

    boolean existsByAccountNumber(String accountNumber);
}
