package com.mybank.mycards.service;

import com.mybank.common.entity.Card;
import com.mybank.common.entity.Customer;
import com.mybank.mycards.dto.CardTransactionDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICustomerCardService {
    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);
    List<Card> findCardsByCustomerId(Long customerId);
    List<CardTransactionDTO> getRecentTransactionsByCustomer(String phoneNumber, LocalDate date);
}