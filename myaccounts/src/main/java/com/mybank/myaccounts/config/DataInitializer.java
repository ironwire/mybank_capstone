package com.mybank.myaccounts.config;

import com.mybank.common.entity.Role;
import com.mybank.common.entity.User;
import com.mybank.common.repository.RoleRepository;
import com.mybank.common.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            if (roleRepository.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                adminRole.setDescription("Administrator role");
                roleRepository.save(adminRole);

                Role managerRole = new Role();
                managerRole.setName("MANAGER");
                managerRole.setDescription("Manager role");
                roleRepository.save(managerRole);

                Role userRole = new Role();
                userRole.setName("USER");
                userRole.setDescription("Regular user role");
                roleRepository.save(userRole);

                // Create admin user
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setEmail("admin@mybank.com");
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setIsEnabled(true);

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminUser.setRoles(adminRoles);

                userRepository.save(adminUser);
            }
        };
    }
}