package com.mybank.myaccounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an account has insufficient funds for a transaction
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {
    
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}