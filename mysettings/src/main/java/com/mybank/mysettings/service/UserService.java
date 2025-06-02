package com.mybank.mysettings.service;

import java.util.Optional;

public interface UserService {
    
    /**
     * Get customer ID by user ID
     * 
     * @param userId the ID of the user
     * @return the customer ID if found
     */
    Optional<Long> getCustomerIdByUserId(Long userId);
    
    /**
     * Get user ID by customer ID
     * 
     * @param customerId the ID of the customer
     * @return the user ID if found
     */
    Optional<Long> getUserIdByCustomerId(Long customerId);
    
    /**
     * Check if a user ID is associated with a customer ID
     * 
     * @param userId the ID of the user
     * @return true if the user has an associated customer, false otherwise
     */
    boolean hasCustomer(Long userId);
}