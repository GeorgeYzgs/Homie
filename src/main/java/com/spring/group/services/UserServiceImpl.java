package com.spring.group.services;

import com.spring.group.dto.user.RegisterUserDto;
import com.spring.group.models.user.User;
import com.spring.group.repos.UserRepository;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public User insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> checkUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> checkEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByID(Integer userID) { return userRepository.getOne(userID); }

    @Override
    public Collection<User> getUserList() { return userRepository.findAll(); }

    public boolean isUsernamePresent(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailPresent(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String registerUser(RegisterUserDto dto) {
        if (checkUserName(dto.getUsername()).isPresent()) {
            return messageSource.getMessage("Username.unavailable", null, Locale.UK);
        }
        if (checkEmail(dto.getEmail()).isPresent()) {
            return messageSource.getMessage("Email.unavailable", null, Locale.UK);
        }
        User user = dto.unwrapDTO();
        insertUser(user);
        tokenService.createConfirmEmail(user);
        return "SUCCESS";
    }

    public String changePass(RegisterUserDto dto, int userID) {
        User user = getUserByID(userID);
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return messageSource.getMessage("Password.not.matches.old.password", null, Locale.UK);
        }
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return messageSource.getMessage("Password.cannot.match.old.password", null, Locale.UK);
        }
        user.setPassword(dto.getPassword());
        insertUser(user);
        return "SUCCESS";
    }
}
