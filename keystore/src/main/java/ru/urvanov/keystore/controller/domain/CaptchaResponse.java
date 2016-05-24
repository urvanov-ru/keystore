package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;

public class CaptchaResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1833894317992816517L;
    private boolean success;
    private String captchaHtml;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCaptchaHtml() {
        return captchaHtml;
    }

    public void setCaptchaHtml(String captchaHtml) {
        this.captchaHtml = captchaHtml;
    }
}
