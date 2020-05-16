package com.spring.group.models.user;

/**
 * @author George.Giazitzis
 * Enumerated values that are used from account creation, to differentiate between local (Homie) accounts via form login
 * and accounts created via oauth login and social networks
 */
public enum AuthProvider {

    Homie,
    facebook,
    github,
    google;
}
