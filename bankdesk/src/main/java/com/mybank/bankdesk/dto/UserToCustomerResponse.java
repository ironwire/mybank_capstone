package com.mybank.bankdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToCustomerResponse {
    private Map<Long, Long> userToCustomerMap;
    private String message;
}