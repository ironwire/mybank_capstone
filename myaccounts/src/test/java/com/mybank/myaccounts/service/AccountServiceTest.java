package com.mybank.myaccounts.service;

import com.mybank.common.entity.Account;
import com.mybank.myaccounts.exception.ResourceNotFoundException;
import com.mybank.myaccounts.repository.AccountRepository;
import com.mybank.myaccounts.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        // Any setup code if needed
    }

    @Test
    void getAccountBalancesByUserId_ShouldReturnBalancesMap() {
        // Arrange
        Long userId = 1L;
        Long customerId = 100L;
        
        Account account1 = new Account();
        account1.setAccountNumber("ACC123");
        account1.setBalance(new BigDecimal("1000.00"));
        
        Account account2 = new Account();
        account2.setAccountNumber("ACC456");
        account2.setBalance(new BigDecimal("2500.50"));
        
        List<Account> accounts = Arrays.asList(account1, account2);
        
        when(userService.getCustomerIdByUserId(userId)).thenReturn(Optional.of(customerId));
        when(accountRepository.findByCustomerCustomerId(customerId)).thenReturn(accounts);
        
        // Act
        Map<String, BigDecimal> result = accountService.getAccountBalancesByUserId(userId);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("1000.00"), result.get("ACC123"));
        assertEquals(new BigDecimal("2500.50"), result.get("ACC456"));
        
        verify(userService).getCustomerIdByUserId(userId);
        verify(accountRepository).findByCustomerCustomerId(customerId);
    }

    @Test
    void getAccountBalancesByUserId_WhenUserHasNoCustomer_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        when(userService.getCustomerIdByUserId(userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountBalancesByUserId(userId);
        });
        
        verify(userService).getCustomerIdByUserId(userId);
        verifyNoInteractions(accountRepository);
    }
}