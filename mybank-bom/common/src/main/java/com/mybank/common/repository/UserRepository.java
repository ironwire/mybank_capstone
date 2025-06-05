package com.mybank.common.repository;

import com.mybank.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByCustomerCustomerId(Long customerId);
    
    /**
     * Find customer ID by user ID
     * 
     * @param userId the ID of the user
     * @return the customer ID associated with the user
     */
    @Query("SELECT u.customer.customerId FROM User u WHERE u.userId = :userId")
    Optional<Long> findCustomerIdByUserId(@Param("userId") Long userId);
}
