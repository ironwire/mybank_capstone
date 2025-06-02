package com.mybank.mysettings.repository;

import com.mybank.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    /**
     * Find customer ID by user ID
     * 
     * @param userId the ID of the user
     * @return the customer ID associated with the user
     */
    @Query("SELECT u.customer.customerId FROM User u WHERE u.userId = :userId")
    Optional<Long> findCustomerIdByUserId(@Param("userId") Long userId);
    
    /**
     * Find user by customer ID
     * 
     * @param customerId the ID of the customer
     * @return the user associated with the customer
     */
    Optional<User> findByCustomerCustomerId(Long customerId);
}