package com.mybank.mycards.service;

import com.mybank.mycards.dto.CardDTO;

import java.util.List;

public interface IUserCardService {
    /**
     * Get all cards for the current authenticated user
     * @return List of card DTOs
     */
    List<CardDTO> getCardsForCurrentUser();
    
    /**
     * Get all cards for a specific user by user ID
     * @param userId the user ID
     * @return List of card DTOs
     */
    List<CardDTO> getCardsByUserId(Long userId);
    
    /**
     * Get all cards for a specific customer by phone number
     * @param phoneNumber the customer's phone number
     * @return List of card DTOs
     */
    List<CardDTO> getCardsByPhoneNumber(String phoneNumber);
}