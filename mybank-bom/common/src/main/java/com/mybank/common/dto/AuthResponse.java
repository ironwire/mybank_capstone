package com.mybank.common.dto;

import lombok.Data;
import java.util.List;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<String> roles;
    
    // Simple constructor for backward compatibility
    public AuthResponse(String token) {
        this.token = token;
    }
    
    // Full constructor
    public AuthResponse(String token, Long userId, String username, String email, 
                        String firstName, String lastName, String phoneNumber, List<String> roles) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}