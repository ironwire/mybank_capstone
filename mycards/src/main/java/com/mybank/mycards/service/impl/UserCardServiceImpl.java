package com.mybank.mycards.service.impl;

import com.mybank.common.entity.Card;
import com.mybank.common.entity.Customer;
import com.mybank.common.security.SecurityUtils;
import com.mybank.mycards.dto.CardDTO;
import com.mybank.mycards.mapper.CardMapper;
import com.mybank.mycards.repository.CardRepository;
import com.mybank.mycards.repository.CustomerRepository;
import com.mybank.mycards.service.IUserCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCardServiceImpl implements IUserCardService {

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final CardMapper cardMapper;
    private final SecurityUtils securityUtils;

    @Autowired
    public UserCardServiceImpl(
            CardRepository cardRepository,
            CustomerRepository customerRepository,
            CardMapper cardMapper,
            SecurityUtils securityUtils) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
        this.cardMapper = cardMapper;
        this.securityUtils = securityUtils;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsForCurrentUser() {
        // Get current username (phone number in our case)
        String phoneNumber = securityUtils.getCurrentUserPhoneNumber();
        if (phoneNumber == null) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        
        return getCardsByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByUserId(Long userId) {
        // In a real implementation, you would look up the customer by user ID
        // For now, we'll throw an exception as this requires integration with the user service
        throw new UnsupportedOperationException("Getting cards by user ID is not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByPhoneNumber(String phoneNumber) {
        // Find customer by phone number
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + phoneNumber));
        
        // Find all cards for this customer
        List<Card> customerCards = cardRepository.findByCustomerCustomerId(customer.getCustomerId());
        
        if (customerCards.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Convert to DTOs
        return cardMapper.toDTOList(customerCards);
    }
}