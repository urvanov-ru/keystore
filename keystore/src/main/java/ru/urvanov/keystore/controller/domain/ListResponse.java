package ru.urvanov.keystore.controller.domain;

import java.util.ArrayList;
import java.util.List;

public class ListResponse<T> extends SimpleResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 7959359570700772296L;
    private List<T> info = new ArrayList<T>();

    private Long totalRecords = new Long(0);

    public List<T> getInfo() {
        return info;
    }

    public void setInfo(List<T> info) {
        this.info = info;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

}
