package com.spring.group.services;

import com.spring.group.models.user.ConfirmationToken;
import com.spring.group.models.user.ResetPassToken;
import com.spring.group.models.user.User;
import com.spring.group.repos.ConfirmationTokenRepository;
import com.spring.group.repos.ResetPassTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class TokenService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private ResetPassTokenRepository resetPassTokenRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public Optional<ConfirmationToken> checkConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    public Optional<ResetPassToken> checkResetPassToken(String token) {
        return resetPassTokenRepository.findByResetPassToken(token);
    }

    public void createConfirmEmail(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("homieafdemp@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/GroupProject/confirm-account/" + confirmationToken.getConfirmationToken());
        sendEmail(mailMessage);
    }

    public void createResetEmail(User user) {
        ResetPassToken resetPassToken = new ResetPassToken(user);
        resetPassTokenRepository.save(resetPassToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Reset Password!");
        mailMessage.setFrom("homieafdemp@gmail.com");
        mailMessage.setText("To reset your password, please click here : "
                + "http://localhost:8080/GroupProject/reset-password/" + resetPassToken.getResetPassToken());
        sendEmail(mailMessage);
    }

    public String validateConfirmationToken(String confirmationToken) {
        Optional<ConfirmationToken> token = checkConfirmationToken(confirmationToken);
        if (!token.isPresent()) {
            return "This is an invalid token";
        }
        ConfirmationToken validToken = token.get();
        User user = userServiceImpl.checkEmail(validToken.getUser().getEmail()).get();
        if (validToken.getExpirationDate().isBefore(Instant.now())) {
            createConfirmEmail(user);
            return "This token has expired, a new token has been emailed to you";
        }
        user.setEnabled(true);
        userServiceImpl.updateUser(user);
        return "SUCCESS";
    }

    public String validateResetToken(String resetPassToken) {
        Optional<ResetPassToken> token = checkResetPassToken(resetPassToken);
        if (!token.isPresent()) {
            return "This is an invalid token";
        }
        ResetPassToken validToken = token.get();
        if (validToken.isUsed()) {
            return "This token has already been used";
        }
        if (validToken.getExpirationDate().isBefore(Instant.now())) {
            return "This token has expired, please request another token";
        }
        validToken.setUsed(true);
        resetPassTokenRepository.save(validToken);
        return validToken.getUser().getEmail();
    }
}
