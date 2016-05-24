package ru.urvanov.keystore.domain;

import java.util.Date;

public class KeyListParameters {
    private Long userId;
    private String clientName;
    private KeyStatus status;
    private Date activeOnDate;
    private Long orderId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public KeyStatus getStatus() {
        return status;
    }

    public void setStatus(KeyStatus status) {
        this.status = status;
    }

    public Date getActiveOnDate() {
        return activeOnDate;
    }

    public void setActiveOnDate(Date activeOnDate) {
        this.activeOnDate = activeOnDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
