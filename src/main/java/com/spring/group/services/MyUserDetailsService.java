package com.spring.group.services;

import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username.toLowerCase());
        userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return userOptional.map(MyUserDetails::new).get();
    }
}
