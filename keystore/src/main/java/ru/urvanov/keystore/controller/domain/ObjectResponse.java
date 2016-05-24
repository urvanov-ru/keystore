package ru.urvanov.keystore.controller.domain;

public class ObjectResponse<T> extends SimpleResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -6693400915031811231L;
    private T info;

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

}
