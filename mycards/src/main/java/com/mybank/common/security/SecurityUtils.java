package com.mybank.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.mybank.common.entity.User;
import java.util.Map;

@Component
public class SecurityUtils {

    /**
     * Get the username of the currently authenticated user
     * @return the username or null if no user is authenticated
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // First try to get username from principal
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }

        // If principal doesn't have username, try to get it from the name
        return authentication.getName();
    }

    /**
     * Get the current authenticated user as a User object
     * @return the User object or null if no user is authenticated
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof UserDetails) {
            // Create a new User object with information from UserDetails
            User user = new User();
            user.setUsername(((UserDetails) principal).getUsername());
            return user;
        }

        return null;
    }

    /**
     * Get the user ID of the currently authenticated user
     * @return the user ID or null if not available
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // If principal is our custom User object
        if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getUserId();
        }

        // Try to get from details if available
        if (authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            if (details.containsKey("userId")) {
                return Long.valueOf(details.get("userId").toString());
            }
        }

        return null;
    }

    public String getCurrentUserPhoneNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // If principal is our custom User object
        if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getPhoneNumber();
        }

        // Try to get from details if available
        if (authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            if (details.containsKey("phoneNumber")) {
                return details.get("phoneNumber").toString();
            }
        }

        return null;
    }


}