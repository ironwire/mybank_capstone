package com.mybank.myloans.service;

import com.mybank.myloans.dto.LoanTypeDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LoanTypeService {
    
    /**
     * Get all loan types
     * 
     * @return list of all loan type DTOs
     */
    List<LoanTypeDTO> getAllLoanTypes();
    
    /**
     * Get all active loan types
     * 
     * @return list of active loan type DTOs
     */
    List<LoanTypeDTO> getActiveLoanTypes();
    
    /**
     * Get a loan type by ID
     * 
     * @param loanTypeId the ID of the loan type
     * @return the loan type DTO if found, or empty optional
     */
    Optional<LoanTypeDTO> getLoanTypeById(Long loanTypeId);
    
    /**
     * Get a loan type by name
     * 
     * @param name the name of the loan type
     * @return the loan type DTO if found, or empty optional
     */
    Optional<LoanTypeDTO> getLoanTypeByName(String name);
    
    /**
     * Get loan types suitable for the specified loan amount
     * 
     * @param amount the loan amount
     * @return list of loan type DTOs suitable for the specified amount
     */
    List<LoanTypeDTO> getLoanTypesForAmount(BigDecimal amount);
    
    /**
     * Get loan types with interest rate in the specified range
     * 
     * @param minRate the minimum interest rate
     * @param maxRate the maximum interest rate
     * @return list of loan type DTOs with interest rate in the specified range
     */
    List<LoanTypeDTO> getLoanTypesByInterestRateRange(BigDecimal minRate, BigDecimal maxRate);
}