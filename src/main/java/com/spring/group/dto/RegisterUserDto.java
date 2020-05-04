package com.spring.group.dto;

import com.spring.group.services.MyUserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author George.Giazitzis
 */
@PasswordMatches
public class RegisterUserDto {

    @NotBlank
    @Size(min = 4, max = 25)
    private String username;
    @ValidEmail
    private String email;
    @NotBlank
    @Size(min = 4, max = 25)
    private String password;
    private String password2;

    public RegisterUserDto(MyUserDetails loggedUser) {
        this.username = loggedUser.getUsername();
        this.email = loggedUser.getEmail();
    }

    public RegisterUserDto() {
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

}
