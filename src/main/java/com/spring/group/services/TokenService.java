package com.spring.group.services;

import com.spring.group.dto.user.RegisterUserDto;
import com.spring.group.models.user.AuthProvider;
import com.spring.group.models.user.ConfirmationToken;
import com.spring.group.models.user.ResetPassToken;
import com.spring.group.models.user.User;
import com.spring.group.repos.ConfirmationTokenRepository;
import com.spring.group.repos.ResetPassTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Locale;
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
    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * Utilizes java mail sender to send an async, yes java async, email.
     *
     * @param email the email to be sent.
     */
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    /**
     * Utilizes java mail sender to send an async, yes java async, email.
     *
     * @param email the email to be sent.
     */
    @Async
    public void sendEmail(MimeMessage email) {
        javaMailSender.send(email);
    }


    public Optional<ConfirmationToken> checkConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    public Optional<ResetPassToken> checkResetPassToken(String token) {
        return resetPassTokenRepository.findByResetPassToken(token);
    }

    /**
     * Creates a confirmation token for the provided user, persists it in our database
     * and creates an email to be sent to the given user to fully activate his account.
     *
     * @param user the user the created confirmation token and email will be linked to
     */
    public void createConfirmEmail(User user, Locale userLocale) throws MessagingException {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        MimeMessage emailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(user.getEmail());
        helper.setSubject("Complete Registration!");
        helper.setFrom("homieafdemp@gmail.com");
        Context ctx = new Context(userLocale);
        String tokenString =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                        + "/confirm-account/" + confirmationToken.getConfirmationToken();
        ctx.setVariable("userName", user.getUsername());
        ctx.setVariable("tokenLink", tokenString);
        String mailBody = templateEngine.process("mail/register-mail", ctx);
        helper.setText(mailBody, true);
        helper.addInline("logo.png",
                new ClassPathResource("static/img/logo-house-green.png"));
        sendEmail(emailMessage);
    }

    /**
     * Creates a reset pass token for the provided user, persists it in our database
     * and creates an email to be sent to the given user to reset their password.
     *
     * @param user the user the created reset token and email will be linked to
     */
    public void createResetEmail(User user, Locale userLocale) throws MessagingException {
        ResetPassToken resetPassToken = new ResetPassToken(user);
        resetPassTokenRepository.save(resetPassToken);
        MimeMessage emailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(user.getEmail());
        helper.setSubject("Reset Password!");
        helper.setFrom("homieafdemp@gmail.com");
        Context ctx = new Context(userLocale);
        String tokenString = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                + "/reset-password/" + resetPassToken.getResetPassToken();
        ctx.setVariable("userName", user.getUsername());
        ctx.setVariable("tokenLink", tokenString);
        ctx.setVariable("expirationHours", ResetPassToken.getExpirationHours());
        String mailBody = templateEngine.process("mail/reset-mail", ctx);
        helper.setText(mailBody, true);
        helper.addInline("logo.png",
                new ClassPathResource("static/img/logo-house-green.png"));
        sendEmail(emailMessage);

    }

    /**
     * Validates that the given confirmation token exists in our database and is not expired.
     * If expired, issues a new confirmation token for the user linked to provided token
     * Returns "SUCCESS" if the above criteria are met, otherwise an error message.
     *
     * @param confirmationToken the token to be validated
     * @return "SUCCESS" or an error message as a string
     */
    public String validateConfirmationToken(String confirmationToken, Locale userLocale) throws MessagingException {
        Optional<ConfirmationToken> token = checkConfirmationToken(confirmationToken);
        if (!token.isPresent()) {
            return "token.invalid";
        }
        ConfirmationToken validToken = token.get();
        User user = userServiceImpl.checkEmail(validToken.getUser().getEmail()).get();
        if (validToken.getExpirationDate().isBefore(Instant.now())) {
            createConfirmEmail(user, userLocale);
            return "token.expired.mail";
        }
        user.setEnabled(true);
        userServiceImpl.updateUser(user);
        return "SUCCESS";
    }

    /**
     * Validates that the given reset token exists in our database, has not been used
     * and is not expired.Returns the email of the user linked to the above token if successful, otherwise
     * an error message
     *
     * @param resetPassToken
     * @return
     */
    public String validateResetToken(String resetPassToken) {
        Optional<ResetPassToken> token = checkResetPassToken(resetPassToken);
        if (!token.isPresent()) {
            return "token.invalid";
        }
        ResetPassToken validToken = token.get();
        if (validToken.isUsed()) {
            return "token.used";
        }
        if (validToken.getExpirationDate().isBefore(Instant.now())) {
            return "token.expired.request";
        }
        return validToken.getUser().getEmail();
    }

    /**
     * Validates that the given email is linked to an account provided,
     * and if the linked account is a local Homie account
     *
     * @param email the email provided
     * @return "SUCCESS" if above criteria are met, otherwise an error message
     */
    public String forgotPass(String email, Locale userLocale) throws MessagingException {
        Optional<User> user = userServiceImpl.checkEmail(email);
        if (!user.isPresent()) {
            return "token.noAccount";
        }
        if (!user.get().getAuthProvider().equals(AuthProvider.Homie)) {
            return "token.noReset";
        }
        createResetEmail(user.get(), userLocale);
        return "SUCCESS";
    }

    /**
     * Unwraps the given data access object and the provided email to set a new pass to the user linked to it.
     *
     * @param dto   the data access object to be unwrapped
     * @param email the provided email address
     */
    public void setNewPass(RegisterUserDto dto, String email) {
        User user = userServiceImpl.checkEmail(email).get();
        user.setPassword(dto.getPassword());
        userServiceImpl.insertUser(user);
        ResetPassToken token = checkResetPassToken(dto.getToken()).get();
        token.setUsed(true);
        resetPassTokenRepository.save(token);
    }

    //TODO payment template.
    public void informPayment(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Rent Paid!");
        mailMessage.setFrom("homieafdemp@gmail.com");
        mailMessage.setText("You have successfully paid rent for the month of " + Instant.now().atZone(ZoneOffset.UTC).getMonth());
        sendEmail(mailMessage);
    }
}
