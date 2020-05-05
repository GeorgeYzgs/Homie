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

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Service
public class TokenService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private ResetPassTokenRepository resetPassTokenRepository;

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
}
