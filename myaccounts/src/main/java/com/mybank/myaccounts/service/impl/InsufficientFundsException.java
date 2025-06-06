package com.mybank.myaccounts.service.impl;

public class InsufficientFundsException extends Throwable {
    private String insufficientFundsForWithdrawal="余额不足。";

    public InsufficientFundsException(String insufficientFundsForWithdrawal) {
        this.insufficientFundsForWithdrawal = insufficientFundsForWithdrawal;
    }
}
