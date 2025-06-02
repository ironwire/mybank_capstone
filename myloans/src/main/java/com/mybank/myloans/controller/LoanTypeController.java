package com.mybank.myloans.controller;

import com.mybank.myloans.dto.LoanTypeDTO;
import com.mybank.myloans.service.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/myloans")
public class LoanTypeController {

    private final LoanTypeService loanTypeService;

    @Autowired
    public LoanTypeController(LoanTypeService loanTypeService) {
        this.loanTypeService = loanTypeService;
    }

    @GetMapping("/loan-types")
    public ResponseEntity<List<LoanTypeDTO>> getAllLoanTypes() {
        List<LoanTypeDTO> loanTypes = loanTypeService.getAllLoanTypes();
        return ResponseEntity.ok(loanTypes);
    }

    @GetMapping("/loan-types/active")
    public ResponseEntity<List<LoanTypeDTO>> getActiveLoanTypes() {
        List<LoanTypeDTO> loanTypes = loanTypeService.getActiveLoanTypes();
        return ResponseEntity.ok(loanTypes);
    }

    @GetMapping("/loan-types/{loanTypeId}")
    public ResponseEntity<LoanTypeDTO> getLoanTypeById(
            @PathVariable("loanTypeId") Long loanTypeId) {
        return loanTypeService.getLoanTypeById(loanTypeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/loan-types/name/{name}")
    public ResponseEntity<LoanTypeDTO> getLoanTypeByName(
            @PathVariable("name") String name) {
        return loanTypeService.getLoanTypeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/loan-types/for-amount")
    public ResponseEntity<List<LoanTypeDTO>> getLoanTypesForAmount(
            @RequestParam("amount") BigDecimal amount) {
        List<LoanTypeDTO> loanTypes = loanTypeService.getLoanTypesForAmount(amount);
        return ResponseEntity.ok(loanTypes);
    }

    @GetMapping("/loan-types/by-interest-rate")
    public ResponseEntity<List<LoanTypeDTO>> getLoanTypesByInterestRateRange(
            @RequestParam("minRate") BigDecimal minRate,
            @RequestParam("maxRate") BigDecimal maxRate) {
        List<LoanTypeDTO> loanTypes = loanTypeService.getLoanTypesByInterestRateRange(minRate, maxRate);
        return ResponseEntity.ok(loanTypes);
    }
}