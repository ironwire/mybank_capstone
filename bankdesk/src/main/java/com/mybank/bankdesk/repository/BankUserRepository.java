package com.mybank.bankdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.mybank.common.entity.User;

import java.util.List;

@Repository
public interface BankUserRepository extends JpaRepository<User,Long> {
    
    /**
     * Find all users who have a null customerId and have the USER role
     * 
     * @return List of users with null customerId and USER role
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.customer.customerId IS NULL AND r.name = 'USER'")
    List<User> findUsersWithNullCustomerIdAndUserRole();
}
