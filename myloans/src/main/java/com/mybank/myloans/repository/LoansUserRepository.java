package com.mybank.myloans.repository;

import com.mybank.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoansUserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by ID and return the associated customer ID
     * 
     * @param userId the ID of the user
     * @return the user with the specified ID
     */
    Optional<User> findById(Long userId);
    
    /**
     * Find a user by username
     * 
     * @param username the username
     * @return the user with the specified username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find customer ID by user ID
     * 
     * @param userId the ID of the user
     * @return the customer ID associated with the user
     */
    @Query("SELECT u.customer.customerId FROM User u WHERE u.userId = :userId")
    Optional<Long> findCustomerIdByUserId(@Param("userId") Long userId);
}