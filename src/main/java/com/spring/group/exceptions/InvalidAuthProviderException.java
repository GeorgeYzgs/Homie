package com.spring.group.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author George.Giazitzis
 */
public class InvalidAuthProviderException extends AuthenticationException {

    public InvalidAuthProviderException(String msg) {
        super(msg);
    }
}
