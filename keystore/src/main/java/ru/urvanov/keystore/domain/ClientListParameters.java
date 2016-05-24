package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;

public class ClientListParameters implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8776794361564480227L;

    private Long userId;
    private Boolean active;
    private Long dictClientGroupId;
    private Date activeBegin;
    private Date activeEnd;
    private Long dictActionId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getDictClientGroupId() {
        return dictClientGroupId;
    }

    public void setDictClientGroupId(Long dictClientGroupId) {
        this.dictClientGroupId = dictClientGroupId;
    }

    public Date getActiveBegin() {
        return activeBegin;
    }

    public void setActiveBegin(Date activeBegin) {
        this.activeBegin = activeBegin;
    }

    public Date getActiveEnd() {
        return activeEnd;
    }

    public void setActiveEnd(Date activeEnd) {
        this.activeEnd = activeEnd;
    }
    
    public Long getDictActionId() {
        return dictActionId;
    }

    public void setDictActionId(Long dictActionId) {
        this.dictActionId = dictActionId;
    }

    @Override
    public String toString() {
        return "ClientListParameters userId=" + userId + ", active=" + active
                + ", dictClientGroupId=" + dictClientGroupId + ", activeBegin="
                + activeBegin + ", activeEnd=" + activeEnd;
    }
}
