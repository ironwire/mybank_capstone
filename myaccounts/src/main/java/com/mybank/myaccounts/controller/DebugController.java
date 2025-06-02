package com.mybank.myaccounts.controller;

import com.mybank.common.security.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/cards/")
@CrossOrigin(origins = "*")
public class DebugController {

    @GetMapping("public/debug")
    public Map<String, Object> debug() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Debug endpoint is working");
        
        // Get current authentication if any
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            response.put("authenticated", auth.isAuthenticated());
            response.put("principal", auth.getPrincipal().toString());
            response.put("authorities", auth.getAuthorities().toString());
        } else {
            response.put("authenticated", false);
        }
        
        return response;
    }
    
    @PostMapping("private/debug-post")
    public Map<String, Object> debugPost(@RequestBody(required = false) Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "POST endpoint is working");
        response.put("received", payload != null ? payload.toString() : "no payload");
        
        return response;
    }

    @GetMapping("/debug-token")
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
                response.put("roles", claims.get("roles"));
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

    @GetMapping("transactions/recent-transactions")
    public Map<String, Object> testAuth(@RequestParam(name="phoneNumber") String phoneNumber,@RequestParam(name="date") String date) {
        Map<String, Object> response = new HashMap<>();
        response.put("Phone Number", phoneNumber);
        response.put("Date", date);

        return response;
    }
}
