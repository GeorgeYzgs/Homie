package com.spring.group.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * @author George.Giazitzis
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * An array of all the endpoints the user can only access upon being authenticated
     */
    private static final String[] LOGGED_USER_URLS = {"/manage-offers", "/submit-offer", "/my-profile", "/async/**",
            "/change-pass", "/list-new-property"};

    /**
     * The user detail service class used for logging in
     */
    @Qualifier("myUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configures the authentication builder to utilize our implementation of a login service class
     * and a password encoder
     *
     * @param auth the authentication manager builder, default provided by spring security
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    /**
     * The main security configuration of endpoints, separates endpoints into admin only access,
     * moderator, users and permit all, along with enabling both form and oauth logins.
     * Furthermore, allows only one session per user and enables session registry.
     *
     * @param http the http security to be configured
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/mod/**").hasAnyAuthority("ADMIN", "MODERATOR")
                .antMatchers(LOGGED_USER_URLS).authenticated()
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/login")
                .and().oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login?logout")
                .logoutSuccessUrl("/")
                .and().rememberMe();

        http.sessionManagement()
                .maximumSessions(1).sessionRegistry(sessionRegistry());
    }

    /**
     * The chosen implementation of a password encoder
     *
     * @return a bcrypt password encoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * A session registry to be used for monitoring user activity
     *
     * @return a session registry implementation
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
