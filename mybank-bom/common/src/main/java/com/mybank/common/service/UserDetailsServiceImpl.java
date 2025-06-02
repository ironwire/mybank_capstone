package com.mybank.common.service;

import com.mybank.common.entity.Role;
import com.mybank.common.entity.User;
import com.mybank.common.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Update last login time
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        // Create authorities with both ROLE_ prefix and without prefix for flexibility
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        for (Role role : user.getRoles()) {
            // Add with ROLE_ prefix for hasRole() method
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            // Add without prefix for hasAuthority() method
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getIsEnabled(),
                true, true, true,
                authorities);
    }
}