package com.spring.group.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Class containing beans needed for configuring Internalization in Spring
 */
@Configuration
public class InternalizationConfiguration implements WebMvcConfigurer {

    /**
     * This bean sets the Locale from the browser's settings. It is kept for the duration of the current session.
     * Default set to greek. This is used then to choose which messages to display
     * from message.properties and ValidationMessages.properties
     *
     * @return sessionLocaleResolver object
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        Locale defaultLocale = new Locale("el", "GR");
        sessionLocaleResolver.setDefaultLocale(defaultLocale);
        return sessionLocaleResolver;
    }

    /**
     * This interceptor changes the Locale of the user when a specific parameter is set in the url of request. Here we set
     * the parameter to be lang, so changing to English would be for example lang=en_GB.
     *
     * @return LocaleChangeInterceptor object
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * Overriding addInterceptors to add the LocaleChangeInterceptor to the Registry
     *
     * @param registry Interceptor Registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
