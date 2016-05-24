package ru.urvanov.keystore.exception;

import org.springframework.security.core.AuthenticationException;

public class NotActivatedException extends AuthenticationException {

    /**
     * 
     */
    private static final long serialVersionUID = -3736623149524315719L;

    public NotActivatedException(String msg) {
        super(msg);
    }

    public NotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }

}
