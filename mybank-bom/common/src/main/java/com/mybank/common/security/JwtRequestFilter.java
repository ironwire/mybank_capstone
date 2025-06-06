package com.mybank.common.security;

import com.mybank.common.entity.Role;
import com.mybank.common.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        
        logger.info("Processing request: {}", requestURI);
        
        // Skip token validation for public endpoints
        if (requestURI.contains("/public/") || requestURI.equals("/error")) {
            logger.info("Skipping authentication for public endpoint: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (!hasAuthorizationBearer(request)) {
            logger.info("No Authorization Bearer token found for request: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);
        logger.info("Found token for request {}: {}", requestURI, token.substring(0, Math.min(10, token.length())) + "...");

        if (!jwtUtil.validateAccessToken(token)) {
            logger.info("Token validation failed for request: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            setAuthenticationContext(token, request);
            logger.info("Authentication set successfully for request: {}", requestURI);
            
            // Log the authentication details
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                logger.info("User '{}' with authorities {} is accessing: {}", 
                    auth.getName(), auth.getAuthorities(), requestURI);
            }
        } catch (Exception e) {
            logger.error("Error setting authentication context: {}", e.getMessage(), e);
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("Authentication set for user: {}", userDetails.getUsername());
    }

    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        Claims claims = jwtUtil.parseClaims(token);
        
        // Extract subject
        String subject = (String) claims.get(Claims.SUBJECT);
        
        // Extract roles
        String rolesString = (String) claims.get("roles");
        
        // Extract additional user attributes
        String firstName = (String) claims.get("firstName");
        String lastName = (String) claims.get("lastName");
        String phoneNumber = (String) claims.get("phoneNumber");
        
        logger.debug("JWT Subject: {}", subject);
        logger.debug("JWT Roles: {}", rolesString);
        logger.debug("JWT Phone Number: {}", phoneNumber);
        
        // Set additional user attributes
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setPhoneNumber(phoneNumber);
        
        // Handle roles safely
        if (rolesString != null && !rolesString.isEmpty()) {
            rolesString = rolesString.replace("[", "").replace("]", "");
            String[] roleNames = rolesString.split(",");

            for (String aRoleName : roleNames) {
                aRoleName = aRoleName.trim();
                if (!aRoleName.isEmpty()) {
                    userDetails.addRole(new Role(aRoleName));
                }
            }
        } else {
            // Add a default role if none is found
            logger.warn("No roles found in token, adding default USER role");
            userDetails.addRole(new Role("USER"));
        }

        // Parse subject safely
        if (subject != null && subject.contains(",")) {
            String[] jwtSubject = subject.split(",");
            if (jwtSubject.length >= 2) {
                try {
                    userDetails.setUserId(Long.parseLong(jwtSubject[0]));
                    userDetails.setEmail(jwtSubject[1]);
                } catch (NumberFormatException e) {
                    logger.error("Error parsing user ID from token: {}", e.getMessage());
                    // Set default values
                    userDetails.setUserId(0L);
                    userDetails.setEmail("unknown@example.com");
                }
            } else {
                logger.error("Invalid subject format in token");
                userDetails.setUserId(0L);
                userDetails.setEmail("unknown@example.com");
            }
        } else {
            logger.error("Subject is null or invalid in token");
            userDetails.setUserId(0L);
            userDetails.setEmail("unknown@example.com");
        }

        return userDetails;
    }
}



