package com.mybank.myloans.mapper;

import com.mybank.common.entity.Loan;
import com.mybank.myloans.dto.LoanDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {

    public LoanDTO toDTO(Loan loan) {
        if (loan == null) {
            return null;
        }

        return LoanDTO.builder()
                .loanId(loan.getLoanId())
                .customerId(loan.getCustomerId())
                .loanTypeName(loan.getLoanType() != null ? loan.getLoanType().getName() : null)
                .principalAmount(loan.getPrincipalAmount())
                .outstandingAmount(loan.getOutstandingAmount())
                .interestRate(loan.getInterestRate())
                .termMonths(loan.getTermMonths())
                .startDate(loan.getStartDate())
                .endDate(loan.getEndDate())
                .paymentFrequency(loan.getPaymentFrequency())
                .monthlyPayment(loan.getMonthlyPayment())
                .status(loan.getStatus())
                .build();
    }

    public List<LoanDTO> toDTOList(List<Loan> loans) {
        if (loans == null) {
            return null;
        }
        
        return loans.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}