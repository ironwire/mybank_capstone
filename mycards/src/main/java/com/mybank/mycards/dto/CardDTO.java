package com.mybank.mycards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {
    private Long cardId;
    private Long customerId;
    private Long accountId;
    private String cardTypeName;
    private String cardNumber;
    private String cardholderName;
    private LocalDate expiryDate;
    private BigDecimal limitAmount;
    private String statusName;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private LocalDateTime issueDate;
    
    // Mask the card number for security
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 16) {
            return cardNumber;
        }
        return "xxxx-xxxx-xxxx-" + cardNumber.substring(12);
    }
}