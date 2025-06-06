package com.mybank.myaccounts.controller;

import com.mybank.common.entity.Account;
import com.mybank.myaccounts.dto.AccountDto;
import com.mybank.myaccounts.mapper.AccountMapper;
import com.mybank.myaccounts.repository.AccountRepository;
import com.mybank.myaccounts.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/public/test")
public class TestAccountController {

    AccountRepository accountRepository;
    AccountMapper accountMapper;
    AccountService accountService;

    public TestAccountController(AccountRepository accountRepository, AccountMapper accountMapper, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.accountService = accountService;
    }

    @GetMapping("/service")
    public ResponseEntity<List<Account>> testService() {
        List<Account> accounts = accountService.findAll();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/service/dto")
    public ResponseEntity<List<AccountDto>> testServiceDto() {
        List<Account> accounts = accountService.findAll();
        List<AccountDto> accountDtos = accountMapper.toDtoList(accounts);

        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<AccountDto>> testDto() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountDtos = accountMapper.toDtoList(accounts);

        return ResponseEntity.ok(accountDtos);
    }
    @GetMapping
    public ResponseEntity<List<Account>> test() {
        List<Account> accounts = accountRepository.findAll();

        return ResponseEntity.ok(accounts);
    }
}
