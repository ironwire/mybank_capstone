package com.mybank.myloans.repository;

import com.mybank.common.entity.LoanType;
import com.mybank.common.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Find all loans by customer ID
     * 
     * @param customerId the ID of the customer
     * @return list of loans belonging to the customer
     */
    List<Loan> findByCustomerId(Long customerId);
    
    /**
     * Find all active loans by customer ID
     * 
     * @param customerId the ID of the customer
     * @return list of active loans belonging to the customer
     */
    List<Loan> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find loans with outstanding amount greater than the specified amount
     * 
     * @param amount the minimum outstanding amount
     * @return list of loans with outstanding amount greater than the specified amount
     */
    @Query("SELECT l FROM Loan l WHERE l.outstandingAmount > :amount")
    List<Loan> findLoansWithOutstandingAmountGreaterThan(@Param("amount") BigDecimal amount);
    
    /**
     * Find loans by loan type
     * 
     * @param loanType the loan type
     * @return list of loans of the specified type
     */
    List<Loan> findByLoanType(LoanType loanType);
    
    /**
     * Find loans by loan type name
     * 
     * @param loanTypeName the name of the loan type
     * @return list of loans of the specified type
     */
    @Query("SELECT l FROM Loan l WHERE l.loanType.name = :loanTypeName")
    List<Loan> findByLoanTypeName(@Param("loanTypeName") String loanTypeName);
    
    /**
     * Find loans with end date before the specified date
     * 
     * @param date the date to compare with
     * @return list of loans ending before the specified date
     */
    List<Loan> findByEndDateBefore(LocalDate date);
    
    /**
     * Find loans with start date after the specified date
     * 
     * @param date the date to compare with
     * @return list of loans starting after the specified date
     */
    List<Loan> findByStartDateAfter(LocalDate date);
    
    /**
     * Find loans by customer ID and loan type
     * 
     * @param customerId the ID of the customer
     * @param loanType the loan type
     * @return list of loans belonging to the customer of the specified type
     */
    List<Loan> findByCustomerIdAndLoanType(Long customerId, LoanType loanType);
    
    /**
     * Find loans by customer ID and loan type name
     * 
     * @param customerId the ID of the customer
     * @param loanTypeName the name of the loan type
     * @return list of loans belonging to the customer of the specified type
     */
    @Query("SELECT l FROM Loan l WHERE l.customerId = :customerId AND l.loanType.name = :loanTypeName")
    List<Loan> findByCustomerIdAndLoanTypeName(@Param("customerId") Long customerId, @Param("loanTypeName") String loanTypeName);
}
