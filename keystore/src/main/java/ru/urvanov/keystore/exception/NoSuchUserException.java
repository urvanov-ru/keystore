package ru.urvanov.keystore.exception;

public class NoSuchUserException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5566450734662341819L;

    public NoSuchUserException() {
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUserException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
