package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;

public class PaymentListParameters implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7454465386809168463L;
    private Long userId;
    private String clientName;
    private PaymentType paymentType;
    private PaymentStatus status;
    private PaymentMethod method;
    private Date createdAtBegin;
    private Date createdAtEnd;
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
    public PaymentType getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    public PaymentStatus getStatus() {
        return status;
    }
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    public PaymentMethod getMethod() {
        return method;
    }
    public void setMethod(PaymentMethod method) {
        this.method = method;
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
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    @Override
    public String toString() {
        return "PaymentListParameters [userId=" + userId + ", clientName="
                + clientName + ", paymentType=" + paymentType + ", status="
                + status + ", method=" + method + ", createdAtBegin="
                + createdAtBegin + ", createdAtEnd=" + createdAtEnd
                + ", orderId=" + orderId + "]";
    }
    
}
