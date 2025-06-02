package com.mybank.common.service;

import com.mybank.common.dto.UserRegistrationDto;
import com.mybank.common.entity.Role;
import com.mybank.common.entity.User;
import com.mybank.common.repository.RoleRepository;
import com.mybank.common.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                       RoleRepository roleRepository, 
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .phoneNumber(registrationDto.getPhoneNumber())
                .isEnabled(true)
                .build();

        // First save the user without roles
        user = userRepository.save(user);
        
        // Create a new set for roles
        Set<Role> roles = new HashSet<>();
        
        // Check if roles were provided in the DTO
        if (registrationDto.getRoles() != null && !registrationDto.getRoles().isEmpty()) {
            // Add each role from the DTO
            for (String roleName : registrationDto.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
        } else {
            // If no roles provided, assign default USER role
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found."));
            roles.add(userRole);
        }
        
        // Set the roles and save again
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Transactional
    public User removeRoleFromUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    // Add this utility method to help debug role issues
    public void debugRoles() {
        try {
            List<Role> allRoles = roleRepository.findAll();
            System.out.println("All roles in database:");
            for (Role role : allRoles) {
                System.out.println("Role: " + role.getName() + ", ID: " + role.getRoleId());
            }
            
            Optional<Role> userRole = roleRepository.findByName("USER");
            if (userRole.isPresent()) {
                System.out.println("USER role found with ID: " + userRole.get().getRoleId());
            } else {
                System.out.println("USER role not found in database!");
            }
        } catch (Exception e) {
            System.err.println("Error debugging roles: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
