package com.mybank.mycards.repository;

import com.mybank.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByKeycloakId(String keycloakId);
    
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    List<Customer> findByIsActive(Boolean isActive);

    boolean existsByEmail(String email);
}