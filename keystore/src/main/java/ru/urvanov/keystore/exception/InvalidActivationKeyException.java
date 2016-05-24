package ru.urvanov.keystore.exception;

public class InvalidActivationKeyException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -1454478335230274782L;

    public InvalidActivationKeyException() {
        
    }

    public InvalidActivationKeyException(String message) {
        super(message);
    }

    public InvalidActivationKeyException(Throwable cause) {
        super(cause);
    }

    public InvalidActivationKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidActivationKeyException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
