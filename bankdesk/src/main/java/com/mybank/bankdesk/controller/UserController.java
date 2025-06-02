package com.mybank.bankdesk.controller;

import com.mybank.bankdesk.dto.UserDto;
import com.mybank.bankdesk.dto.UserToCustomerRequest;
import com.mybank.bankdesk.service.BankUserService;
import com.mybank.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bankdesk/users")
public class UserController {

    private final BankUserService userService;

    @Autowired
    public UserController(BankUserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users who have a null customerId and have the USER role
     * This endpoint is restricted to users with ADMIN role
     * 
     * @return List of users with null customerId and USER role
     */
    @GetMapping("/unassigned")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getUnassignedUsers() {
        List<User> users = userService.getUsersWithNullCustomerIdAndUserRole();
        List<UserDto> userDtos = users.stream()
            .map(UserDto::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }
    
    /**
     * Convert users to customers and update user records with customer IDs
     * This endpoint is restricted to users with ADMIN role
     * 
     * @param request The request containing user IDs to convert
     * @return Map of user IDs to their corresponding customer IDs
     */
    @PostMapping("/convert-to-customers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<Long, Long>> convertUsersToCustomers(@RequestBody UserToCustomerRequest request) {
        Map<Long, Long> userToCustomerMap = userService.convertUsersToCustomers(request);
        return ResponseEntity.ok(userToCustomerMap);
    }
}
