package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;

public class KeyParamsResponse extends SimpleResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4114625333395962216L;
    private KeyParams keyParams;

    public KeyParams getKeyParams() {
        return keyParams;
    }

    public void setKeyParams(KeyParams keyParams) {
        this.keyParams = keyParams;
    }

}
