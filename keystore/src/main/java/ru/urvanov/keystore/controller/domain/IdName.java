package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;

public class IdName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1287141923866804657L;
    private String id;
    private String name;

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdName(Long id, String name) {
        this(String.valueOf(id), name);
    }

    public String getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = String.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdName id=" + id + ", name=" + name;
    }
}
