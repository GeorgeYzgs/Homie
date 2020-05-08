package com.spring.group.services;

import com.spring.group.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateRegistrationFields {

    @Autowired
    UserRepository userRepository;

    public boolean isUsernamePresent(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailPresent(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
