package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;

public class SimpleResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2047144671626752133L;
    private boolean success;
    private String message;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
