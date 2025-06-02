package com.mybank.myloans.repository;

import com.mybank.common.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {

    /**
     * Find a loan type by name
     * 
     * @param name the name of the loan type
     * @return the loan type if found, or empty optional
     */
    Optional<LoanType> findByName(String name);
    
    /**
     * Find all active loan types
     * 
     * @return list of active loan types
     */
    List<LoanType> findByIsActiveTrue();
    
    /**
     * Find loan types with minimum amount less than or equal to the specified amount
     * 
     * @param amount the amount to compare with
     * @return list of loan types with minimum amount less than or equal to the specified amount
     */
    List<LoanType> findByMinimumAmountLessThanEqual(BigDecimal amount);
    
    /**
     * Find loan types with maximum amount greater than or equal to the specified amount
     * 
     * @param amount the amount to compare with
     * @return list of loan types with maximum amount greater than or equal to the specified amount
     */
    List<LoanType> findByMaximumAmountGreaterThanEqual(BigDecimal amount);
    
    /**
     * Find loan types with interest rate in the specified range
     * 
     * @param minRate the minimum interest rate
     * @param maxRate the maximum interest rate
     * @return list of loan types with interest rate in the specified range
     */
    @Query("SELECT lt FROM LoanType lt WHERE lt.minInterestRate >= :minRate AND lt.maxInterestRate <= :maxRate")
    List<LoanType> findByInterestRateRange(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);
}
