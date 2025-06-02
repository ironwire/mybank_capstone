package com.mybank.myloans.controller;

import com.mybank.myloans.dto.LoanDTO;
import com.mybank.myloans.repository.LoansUserRepository;
//import com.mybank.myloans.repository.LoansUserRepository;
import com.mybank.myloans.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/myloans")
public class LoanController {

    private final LoanService loanService;
    private final LoansUserRepository userRepository;

    @Autowired
    public LoanController(LoanService loanService, LoansUserRepository userRepository) {
        this.loanService = loanService;
        this.userRepository = userRepository;
    }

    @GetMapping("/loans/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getLoansByUserId(
            @PathVariable("userId") Long userId) {

        List<LoanDTO> loans = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/loans/customers/{customerId}")
    public ResponseEntity<List<LoanDTO>> getLoansByCustomerId(
            @PathVariable("customerId") Long customerId) {
        Optional<Long> customerIdOpt = userRepository.findCustomerIdByUserId(customerId);
        if (customerIdOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Long actualCustomerId = customerIdOpt.get();
            List<LoanDTO> loans = loanService.getLoansByCustomerId(actualCustomerId);
            return ResponseEntity.ok(loans);
        }
    }

    @GetMapping("/loans/customer/{customerId}/status/{status}")
    public ResponseEntity<List<LoanDTO>> getLoansByCustomerIdAndStatus(
            @PathVariable("customerId") Long customerId,
            @PathVariable("status") String status) {
        List<LoanDTO> loans = loanService.getLoansByCustomerIdAndStatus(customerId, status);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/loans/customer/{customerId}/type/{loanTypeName}")
    public ResponseEntity<List<LoanDTO>> getLoansByCustomerIdAndLoanTypeName(
            @PathVariable("customerId") Long customerId,
            @PathVariable("loanTypeName") String loanTypeName) {
        List<LoanDTO> loans = loanService.getLoansByCustomerIdAndLoanTypeName(customerId, loanTypeName);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<LoanDTO> getLoanById(
            @PathVariable("loanId") Long loanId) {
        return loanService.getLoanById(loanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}