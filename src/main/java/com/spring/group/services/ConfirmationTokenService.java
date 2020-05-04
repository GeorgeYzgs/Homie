package com.spring.group.services;

import com.spring.group.models.user.ConfirmationToken;
import com.spring.group.models.user.User;
import com.spring.group.repos.ConfirmationTokenRepository;
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
public class ConfirmationTokenService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public Optional<ConfirmationToken> checkToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    public void emailUser(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("homieafdemp@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/GroupProject/confirm-account?token=" + confirmationToken.getConfirmationToken());
        sendEmail(mailMessage);
    }

}
