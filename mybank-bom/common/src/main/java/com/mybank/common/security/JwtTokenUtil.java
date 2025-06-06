package com.mybank.common.security;

import com.mybank.common.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.mybank.common.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class  JwtTokenUtil {


    // Option 1: Generate a secure key programmatically
    //private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Option 2: Use a base64-encoded key from properties (recommended for production)
     //@Value("${jwt.secret:mySecretKey123456789012345678901234567890abcdefghijklmnopqrstuvwxyz}")
     private final String jwtSecret="mySecretKey123456789012345678901234567890abcdefghijklmnopqrstuvwxyz";
     SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    @Value("${jwt.expiration:86400000}") // Default 24 hours
    private Long jwtExpiration=86400000L;

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username);
    }

    // Overloaded method to accept User object
    public String generateAccessToken(User user) {
        // Create a safe string representation of roles
        StringBuilder rolesBuilder = new StringBuilder();
        rolesBuilder.append("[");
        
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            boolean first = true;
            for (Role role : user.getRoles()) {
                if (!first) {
                    rolesBuilder.append(",");
                }
                rolesBuilder.append(role.getName());
                first = false;
            }
        } else {
            // Add default role if none exists
            rolesBuilder.append("USER");
        }
        
        rolesBuilder.append("]");
        
        // Create a map for additional user attributes
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", rolesBuilder.toString());
        
        // Add user attributes to claims
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername()); // Add username to claims
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.format("%s,%s", user.getUserId(), user.getEmail()))
                .setIssuer("com.mybank")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessToken(String username, Map<String, Object> additionalClaims) {
        return generateToken(additionalClaims, username);
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey) // Use the secure key
                .compact();
    }

    // Alternative method if you want to use a string-based secret from properties
    /*
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateTokenWithStringSecret(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    */

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.resolve(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Method for backward compatibility
    public Claims parseClaims(String token) {
        return getAllClaimsFromToken(token);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Method for backward compatibility - validates token without username check
    public Boolean validateAccessToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Alternative validation method that extracts username from token
    public Boolean validateAccessToken(String token, String expectedUsername) {
        return validateToken(token, expectedUsername);
    }

    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}