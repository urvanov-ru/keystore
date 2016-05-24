package ru.urvanov.keystore.controller.domain;

import java.util.ArrayList;
import java.util.List;

public class IdNamesResponse extends SimpleResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -4800921435865138104L;

    private List<IdName> info = new ArrayList<IdName>();

    public List<IdName> getInfo() {
        return info;
    }

    public void setInfo(List<IdName> info) {
        this.info = info;
    }

}
