package com.spring.group.models.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author George.Giazitzis
 * The implementation of two interfaces needed by spring security for authentication and authorization.
 * The userdetails interface is utilized upon form login, while the oauthuser interface is utilized by oauth login
 * Having a single class for both interfaces allows us to not differentiate between the active session tokens.
 */
public class MyUserDetails implements OAuth2User, UserDetails {

    private int id;
    private String username;
    private String password;
    private String email;
    private boolean isEnabled;
    private boolean isNonLocked;
    private List<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private boolean oauth2;

    public MyUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        this.isNonLocked = user.isNonLocked();
        this.email = user.getEmail();
        this.authorities = Arrays.asList(new SimpleGrantedAuthority(user.getUserRole().toString()));
    }

    public MyUserDetails(User user, Map<String, Object> attributes) {
        this(user);
        this.attributes = attributes;
        this.oauth2 = true;
    }

    public boolean isOauth2() { return oauth2; }

    public void setOauth2(boolean oauth2) { this.oauth2 = oauth2; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isNonLocked() {
        return isNonLocked;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return "MyUserDetails{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", isNonLocked=" + isNonLocked +
                ", authorities=" + authorities +
                '}';
    }
}
