package com.mybank.myloans.mapper;

import com.mybank.common.entity.LoanType;
import com.mybank.myloans.dto.LoanTypeDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanTypeMapper {

    public LoanTypeDTO toDTO(LoanType loanType) {
        if (loanType == null) {
            return null;
        }

        return LoanTypeDTO.builder()
                .loanTypeId(loanType.getLoanTypeId())
                .name(loanType.getName())
                .description(loanType.getDescription())
                .minInterestRate(loanType.getMinInterestRate())
                .maxInterestRate(loanType.getMaxInterestRate())
                .minTerm(loanType.getMinTerm())
                .maxTerm(loanType.getMaxTerm())
                .minimumAmount(loanType.getMinimumAmount())
                .maximumAmount(loanType.getMaximumAmount())
                .isActive(loanType.getIsActive())
                .build();
    }

    public List<LoanTypeDTO> toDTOList(List<LoanType> loanTypes) {
        if (loanTypes == null) {
            return null;
        }
        
        return loanTypes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}