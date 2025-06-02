package com.mybank.myloans.service;

import com.mybank.myloans.dto.LoanDTO;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    
    /**
     * Find all loans by user ID
     * First retrieves the customer ID associated with the user ID,
     * then finds all loans for that customer
     * 
     * @param userId the ID of the user
     * @return list of loan DTOs belonging to the customer associated with the user
     */
    List<LoanDTO> getLoansByUserId(Long userId);
    
    /**
     * Find all loans by customer ID
     * 
     * @param customerId the ID of the customer
     * @return list of loan DTOs belonging to the customer
     */
    List<LoanDTO> getLoansByCustomerId(Long customerId);
    
    /**
     * Find all active loans by customer ID
     * 
     * @param customerId the ID of the customer
     * @param status the loan status
     * @return list of loan DTOs belonging to the customer with the specified status
     */
    List<LoanDTO> getLoansByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find loans by customer ID and loan type name
     * 
     * @param customerId the ID of the customer
     * @param loanTypeName the name of the loan type
     * @return list of loan DTOs belonging to the customer of the specified type
     */
    List<LoanDTO> getLoansByCustomerIdAndLoanTypeName(Long customerId, String loanTypeName);
    
    /**
     * Get a loan by ID
     * 
     * @param loanId the ID of the loan
     * @return the loan DTO if found, or empty optional
     */
    Optional<LoanDTO> getLoanById(Long loanId);

    Optional<Long> findCustomerIdByUserId(Long userId);
}