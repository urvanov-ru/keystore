package ru.urvanov.keystore.controller.domain;

import java.util.ArrayList;
import java.util.List;

public class CodeNamesResponse extends SimpleResponse {

    /**
     * 
     */
    private static final long serialVersionUID = -8432612983638656595L;

    private List<CodeName> info = new ArrayList<CodeName>();

    public List<CodeName> getInfo() {
        return info;
    }

    public void setInfo(List<CodeName> info) {
        this.info = info;
    }

}
