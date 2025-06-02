package com.mybank.mycards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTransactionDTO {
    private Long transactionId;
    private Long cardId;
    private String cardNumber;
    private BigDecimal amount;
    private String merchantName;
    private String merchantCategory;
    private String transactionLocation;
    private LocalDateTime transactionDate;
    private String transactionType;
    private String status;
}