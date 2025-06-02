package com.mybank.myloans.service.impl;

import com.mybank.common.entity.Loan;
import com.mybank.common.repository.UserRepository;
import com.mybank.myloans.dto.LoanDTO;
import com.mybank.myloans.mapper.LoanMapper;
import com.mybank.myloans.repository.LoanRepository;
import com.mybank.myloans.repository.LoansUserRepository;
import com.mybank.myloans.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final LoansUserRepository userRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, LoanMapper loanMapper, LoansUserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDTO> getLoansByCustomerId(Long customerId) {
        List<Loan> loans = loanRepository.findByCustomerId(customerId);
        return loanMapper.toDTOList(loans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDTO> getLoansByCustomerIdAndStatus(Long customerId, String status) {
        List<Loan> loans = loanRepository.findByCustomerIdAndStatus(customerId, status);
        return loanMapper.toDTOList(loans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDTO> getLoansByCustomerIdAndLoanTypeName(Long customerId, String loanTypeName) {
        List<Loan> loans = loanRepository.findByCustomerIdAndLoanTypeName(customerId, loanTypeName);
        return loanMapper.toDTOList(loans);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanDTO> getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loanMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDTO> getLoansByUserId(Long userId) {
        // TODO: Implement this method
        return null;
    }
    @Override
    public Optional<Long> findCustomerIdByUserId(Long userId) {
        // TODO: Implement this method
        return userRepository.findCustomerIdByUserId(userId);
    }
}