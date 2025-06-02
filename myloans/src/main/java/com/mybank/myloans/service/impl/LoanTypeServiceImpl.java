package com.mybank.myloans.service.impl;

import com.mybank.common.entity.LoanType;
import com.mybank.myloans.dto.LoanTypeDTO;
import com.mybank.myloans.mapper.LoanTypeMapper;
import com.mybank.myloans.repository.LoanTypeRepository;
import com.mybank.myloans.service.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LoanTypeServiceImpl implements LoanTypeService {

    private final LoanTypeRepository loanTypeRepository;
    private final LoanTypeMapper loanTypeMapper;

    @Autowired
    public LoanTypeServiceImpl(LoanTypeRepository loanTypeRepository, LoanTypeMapper loanTypeMapper) {
        this.loanTypeRepository = loanTypeRepository;
        this.loanTypeMapper = loanTypeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> getAllLoanTypes() {
        List<LoanType> loanTypes = loanTypeRepository.findAll();
        return loanTypeMapper.toDTOList(loanTypes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> getActiveLoanTypes() {
        List<LoanType> loanTypes = loanTypeRepository.findByIsActiveTrue();
        return loanTypeMapper.toDTOList(loanTypes);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanTypeDTO> getLoanTypeById(Long loanTypeId) {
        return loanTypeRepository.findById(loanTypeId)
                .map(loanTypeMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanTypeDTO> getLoanTypeByName(String name) {
        return loanTypeRepository.findByName(name)
                .map(loanTypeMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> getLoanTypesForAmount(BigDecimal amount) {
        List<LoanType> loanTypes = loanTypeRepository.findByMinimumAmountLessThanEqual(amount);
        loanTypes.removeIf(loanType -> loanType.getMaximumAmount().compareTo(amount) < 0);
        return loanTypeMapper.toDTOList(loanTypes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> getLoanTypesByInterestRateRange(BigDecimal minRate, BigDecimal maxRate) {
        List<LoanType> loanTypes = loanTypeRepository.findByInterestRateRange(minRate, maxRate);
        return loanTypeMapper.toDTOList(loanTypes);
    }
}