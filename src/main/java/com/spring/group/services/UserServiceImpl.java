package com.spring.group.services;

import com.spring.group.dto.user.RegisterUserDto;
import com.spring.group.models.user.AuthProvider;
import com.spring.group.models.user.User;
import com.spring.group.repos.UserRepository;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
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
    public User getUserByID(Integer userID) {
        return userRepository.getOne(userID);
    }

    @Override
    public Collection<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Integer userID) {
        return userRepository.findById(userID);
    }

    public boolean isUsernamePresent(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailPresent(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Attempts to register a new user to our database, by unwrapping the given data access object.
     * Returns "SUCCESS" if successful, otherwise an error message
     * if the given username or email address of the data access object are unavailable
     *
     * @param dto the data access object to be unwrapped
     * @return "SUCCESS" or an error message
     */
    public String registerUser(RegisterUserDto dto, Locale userLocale) throws MessagingException {
        if (checkUserName(dto.getUsername()).isPresent()) {
            return "Username.unavailable";
        }
        if (checkEmail(dto.getEmail()).isPresent()) {
            return "Email.unavailable";
        }
        User user = dto.unwrapDTO();
        insertUser(user);
        tokenService.createConfirmEmail(user, userLocale);
        return "SUCCESS";
    }

    /**
     * Attempts to change the password of a logged user,by unwrapping a given data access object.
     * If the user is logged in with a Homie account,their old password does not match their new given password,
     * and the two new given passwords match
     *
     * @param dto    the provided data access object
     * @param userID the id of the logged user to be linked to a user in our database
     * @return
     */
    public String changePass(RegisterUserDto dto, int userID) {
        User user = getUserByID(userID);
        if (!user.getAuthProvider().equals(AuthProvider.Homie)) {
            return "user.homie.password.cannot";
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return "Password.not.matches.old.password";
        }
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return "Password.cannot.match.old.password";
        }
        user.setPassword(dto.getPassword());
        insertUser(user);
        return "SUCCESS";
    }

}
