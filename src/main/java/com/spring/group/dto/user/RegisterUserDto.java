package com.spring.group.dto.user;

import com.spring.group.dto.user.annotations.PasswordMatches;
import com.spring.group.dto.user.annotations.ValidEmail;
import com.spring.group.dto.user.annotations.ValidPassword;
import com.spring.group.dto.user.validationgroups.*;
import com.spring.group.models.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author George.Giazitzis
 */
@PasswordMatches(groups = {RegistrationValidator.class, ChangePassValidator.class, ResetPassValidator.class, RegistrationPassMatchValidator.class})
public class RegisterUserDto {

    @NotBlank(groups = {RegistrationValidator.class, RegistrationUsernameValidator.class}, message = "{Not.blank}")
    @Size(min = 4, max = 25, groups = {RegistrationValidator.class, RegistrationUsernameValidator.class}, message = "{Size}")
    private String username;
    @ValidEmail(groups = {RegistrationValidator.class, RegistrationEmailValidator.class})
    private String email;
    @ValidPassword(groups = {RegistrationValidator.class, ChangePassValidator.class, ResetPassValidator.class})
    @NotBlank(groups = {RegistrationValidator.class, ChangePassValidator.class, ResetPassValidator.class}, message = "{Not.blank}")
    @Size(min = 4, max = 25, groups = {RegistrationValidator.class, ChangePassValidator.class, ResetPassValidator.class, RegistrationPasswordValidator.class}, message = "{Size}")
    private String password;
    private String password2;
    private String oldPassword;

    public RegisterUserDto() {
    }

    public User unwrapDTO() {
        return new User(username, password, email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "RegisterUserDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                '}';
    }
}
