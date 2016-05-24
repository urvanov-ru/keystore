package ru.urvanov.keystore.domain;

import java.util.Date;

public class OrderListParameters {

    private Long userId;
    private String clientName;
    private Date createdAtBegin;
    private Date createdAtEnd;
    private OrderStatus status;

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

    public Date getCreatedAtBegin() {
        return createdAtBegin;
    }

    public void setCreatedAtBegin(Date createdAtBegin) {
        this.createdAtBegin = createdAtBegin;
    }

    public Date getCreatedAtEnd() {
        return createdAtEnd;
    }

    public void setCreatedAtEnd(Date createdAtEnd) {
        this.createdAtEnd = createdAtEnd;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
