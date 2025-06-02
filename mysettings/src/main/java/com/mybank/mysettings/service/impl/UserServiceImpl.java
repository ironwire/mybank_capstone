package com.mybank.mysettings.service.impl;

import com.mybank.common.entity.User;
import com.mybank.mysettings.repository.SettingsUserRepository;
import com.mybank.mysettings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final SettingsUserRepository userRepository;

    @Autowired
    public UserServiceImpl(SettingsUserRepository userRepository) {
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