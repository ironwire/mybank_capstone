package com.mybank.bankdesk.service;

import com.mybank.bankdesk.dto.UserToCustomerRequest;
import com.mybank.bankdesk.repository.BankCustomerRepository;
import com.mybank.bankdesk.repository.BankUserRepository;
import com.mybank.common.entity.Customer;
import com.mybank.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankUserService {

    private final BankUserRepository bankUserRepository;
    private final BankCustomerRepository bankCustomerRepository;

    @Autowired
    public BankUserService(BankUserRepository bankUserRepository, BankCustomerRepository bankCustomerRepository) {
        this.bankUserRepository = bankUserRepository;
        this.bankCustomerRepository = bankCustomerRepository;
    }

    /**
     * Get all users who have a null customerId and have the USER role
     * 
     * @return List of users with null customerId and USER role
     */
    /**
     * Get all users who have a null customerId and have the USER role
     *
     * @return List of users with null customerId and USER role
     */
    public List<User> getUsersWithNullCustomerIdAndUserRole() {
        List<User> users = bankUserRepository.findUsersWithNullCustomerIdAndUserRole();

        // Initialize lazy-loaded collections to prevent LazyInitializationException
        for (User user : users) {
            if (user.getRoles() != null) {
                user.getRoles().size(); // Force initialization
            }
        }

        return users;
    }
    
    /**
     * Convert users to customers and update user records with customer IDs
     * 
     * @param request The request containing user IDs to convert
     * @return Map of user IDs to their corresponding customer IDs
     */
    @Transactional
    public Map<Long, Long> convertUsersToCustomers(UserToCustomerRequest request) {
        List<Long> userIds = request.getUserIds();
        Map<Long, Long> userToCustomerMap = new HashMap<>();
        
        // Fetch all users by their IDs
        List<User> users = bankUserRepository.findAllById(userIds);
        
        // Create customers from users
        List<Customer> newCustomers = new ArrayList<>();
        for (User user : users) {
            // Skip if user already has a customer
            if (user.getCustomer() != null) {
                continue;
            }
            
            // Create new customer from user data
            Customer customer = new Customer();
            customer.setFirstName(user.getFirstName());
            customer.setLastName(user.getLastName());
            customer.setEmail(user.getEmail());
            customer.setPhoneNumber(user.getPhoneNumber());
            customer.setIsActive(true);
            customer.setDateOfBirth(LocalDate.now().minusYears(25));//生造数据，user在注册时候可以输入更多的数据，为了方便就减少了需要输入的数据；
            customer.setAddress("123 新街口 1号");
            customer.setCity("北京市");
            customer.setState("北京市");
            customer.setZipCode("100001");
            customer.setPasswordHash("123456");

            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());
            
            newCustomers.add(customer);
        }
        
        // Save all customers at once
        List<Customer> savedCustomers = bankCustomerRepository.saveAll(newCustomers);
        
        // Update users with customer IDs
        for (int i = 0; i < users.size(); i++) {
            if (i < savedCustomers.size()) {
                User user = users.get(i);
                Customer customer = savedCustomers.get(i);
                
                // Update user with customer reference
                user.setCustomer(customer);
                User updatedUser = bankUserRepository.save(user);
                
                // Add to result map
                userToCustomerMap.put(updatedUser.getUserId(), customer.getCustomerId());
            }
        }
        
        return userToCustomerMap;
    }
}
