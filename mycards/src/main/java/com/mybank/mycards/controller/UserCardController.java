package com.mybank.mycards.controller;

import com.mybank.mycards.dto.CardDTO;
import com.mybank.mycards.service.IUserCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserCardController {

    private static final Logger logger = LoggerFactory.getLogger(UserCardController.class);
    private final IUserCardService userCardService;

    @Autowired
    public UserCardController(IUserCardService userCardService) {
        this.userCardService = userCardService;
    }

    /**
     * Get all cards for the current authenticated user
     * @return List of card DTOs
     */
    @GetMapping("/cards")
    public ResponseEntity<List<CardDTO>> getCurrentUserCards() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Received request to get cards for user: {} with authorities: {}", 
                auth != null ? auth.getName() : "unknown", 
                auth != null ? auth.getAuthorities() : "none");
        
        try {
            List<CardDTO> cards = userCardService.getCardsForCurrentUser();
            logger.info("Successfully retrieved {} cards for user", cards.size());
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            logger.error("Error retrieving cards for user: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get all cards for a customer by phone number
     * This endpoint is primarily for testing or admin use
     * @param phoneNumber the customer's phone number
     * @return List of card DTOs
     */
    @GetMapping("/cards/phone/{phoneNumber}")
    public ResponseEntity<List<CardDTO>> getCardsByPhoneNumber(@PathVariable String phoneNumber) {
        logger.info("Received request to get cards for phone number: {}", phoneNumber);
        try {
            List<CardDTO> cards = userCardService.getCardsByPhoneNumber(phoneNumber);
            logger.info("Successfully retrieved {} cards for phone number: {}", cards.size(), phoneNumber);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            logger.error("Error retrieving cards for phone number {}: {}", phoneNumber, e.getMessage(), e);
            throw e;
        }
    }
}