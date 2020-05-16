package com.spring.group.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author George.Giazitzis
 * An exception created for validated oauth user login / registration
 * Extends the AuthenticationException which is a runtime exception and therefore makes it easy to handle.
 * @see AuthenticationException
 */
public class InvalidAuthProviderException extends AuthenticationException {

    public InvalidAuthProviderException(String msg) {
        super(msg);
    }
}
