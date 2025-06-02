package com.mybank.bankdesk.repository;

import com.mybank.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCustomerRepository extends JpaRepository<Customer,Long> {
}
