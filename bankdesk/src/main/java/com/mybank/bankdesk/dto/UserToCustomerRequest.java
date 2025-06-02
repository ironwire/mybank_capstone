package com.mybank.bankdesk.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserToCustomerRequest {
    private List<Long> userIds;
}