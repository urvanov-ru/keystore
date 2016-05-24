package ru.urvanov.keystore.exception;

public class UserNameNotUniqueException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -742526368478151815L;

    public UserNameNotUniqueException() {
        // TODO Auto-generated constructor stub
    }

    public UserNameNotUniqueException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UserNameNotUniqueException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public UserNameNotUniqueException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UserNameNotUniqueException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
