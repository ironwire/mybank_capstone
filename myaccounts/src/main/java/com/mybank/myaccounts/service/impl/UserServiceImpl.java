package com.mybank.myaccounts.service.impl;

import com.mybank.common.entity.User;
import com.mybank.myaccounts.repository.UserRepository;
import com.mybank.myaccounts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the UserService interface
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> getCustomerIdByUserId(Long userId) {
        return userRepository.findCustomerIdByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> getUserIdByCustomerId(Long customerId) {
        return userRepository.findByCustomerCustomerId(customerId)
                .map(User::getUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCustomer(Long userId) {
        return userRepository.findCustomerIdByUserId(userId).isPresent();
    }
}