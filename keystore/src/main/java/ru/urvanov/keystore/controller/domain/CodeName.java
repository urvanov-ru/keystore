package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;

public class CodeName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1287141923866804657L;
    private String code;
    private String name;

    public CodeName(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CodeName code=" + code + ", name = " + name;
    }
}
