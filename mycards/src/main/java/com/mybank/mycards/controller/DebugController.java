package com.mybank.mycards.controller;

import com.mybank.common.security.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/cards/public")
public class DebugController {

    @GetMapping("/auth-debug")
    public Map<String, Object> debugAuth() {
        Map<String, Object> response = new HashMap<>();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            response.put("authenticated", auth.isAuthenticated());
            response.put("principal", auth.getPrincipal().toString());
            response.put("authorities", auth.getAuthorities().toString());
            response.put("name", auth.getName());
            response.put("details", auth.getDetails() != null ? auth.getDetails().toString() : null);
            response.put("credentials", auth.getCredentials() != null ? "PRESENT" : "NONE");
        } else {
            response.put("authenticated", false);
            response.put("error", "No authentication found");
        }
        
        return response;
    }

    @GetMapping("/token-debug")
    public Map<String, Object> debugToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Get the JWT token util
                JwtTokenUtil jwtUtil = new JwtTokenUtil();
                
                // Parse the token
                Claims claims = jwtUtil.parseClaims(token);
                
                // Add claims to response
                response.put("subject", claims.getSubject());
                response.put("username", claims.get("username"));
                response.put("roles", claims.get("roles"));
                response.put("userId", claims.get("userId"));
                response.put("firstName", claims.get("firstName"));
                response.put("lastName", claims.get("lastName"));
                response.put("issuedAt", claims.getIssuedAt());
                response.put("expiration", claims.getExpiration());
                response.put("valid", jwtUtil.validateAccessToken(token));
                
            } catch (Exception e) {
                response.put("error", "Failed to parse token: " + e.getMessage());
            }
        } else {
            response.put("error", "No Authorization header or not a Bearer token");
        }
        
        return response;
    }
}
