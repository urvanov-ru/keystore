package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class PaymentListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1660463844561632658L;

    @Id
    @Column(name="id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;
    
    @Column(name = "createdAt_out")
    private Date createdAt;
    
    @Column(name = "order_createdAt_out")
    private Date orderCreatedAt;
    
    @Column(name = "order_dict_service_type_name_out")
    private String orderDictServiceTypeName;
    
    @Column(name = "payment_type_out")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    
    @Column(name = "payment_type_name_out")
    private String paymentTypeName;
    
    @Column(name = "status_out")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    @Column(name = "status_name_out")
    private String statusName;
    
    @Column(name = "method_out")
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    
    @Column(name = "method_name_out")
    private String methodName;
    
    @Column(name = "client_name_out")
    private String clientName;
    
    @Column(name = "amount_without_commission_out")
    private BigDecimal amountWithoutCommission;
    
    @Column(name = "amount_with_commission_out")
    private BigDecimal amountWithCommission;
    
    @Column(name = "amount_of_commission_out")
    private BigDecimal amountOfCommission;
    
    @Column(name = "info_out")
    private String info;
    
    @Column(name = "comment_out")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(Date orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public String getOrderDictServiceTypeName() {
        return orderDictServiceTypeName;
    }

    public void setOrderDictServiceTypeName(String orderDictServiceTypeName) {
        this.orderDictServiceTypeName = orderDictServiceTypeName;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getAmountWithoutCommission() {
        return amountWithoutCommission;
    }

    public void setAmountWithoutCommission(BigDecimal amountWithoutComission) {
        this.amountWithoutCommission = amountWithoutComission;
    }

    public BigDecimal getAmountWithCommission() {
        return amountWithCommission;
    }

    public void setAmountWithCommission(BigDecimal amountWithCommission) {
        this.amountWithCommission = amountWithCommission;
    }

    public BigDecimal getAmountOfCommission() {
        return amountOfCommission;
    }

    public void setAmountOfCommission(BigDecimal amountOfCommission) {
        this.amountOfCommission = amountOfCommission;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "PaymentListItem [id=" + id + ", createdAt=" + createdAt
                + ", orderCreatedAt=" + orderCreatedAt
                + ", orderDictServiceTypeName=" + orderDictServiceTypeName
                + ", paymentType=" + paymentType + ", paymentTypeName="
                + paymentTypeName + ", status=" + status + ", statusName="
                + statusName + ", method=" + method + ", methodName="
                + methodName + ", clientName=" + clientName
                + ", amountWithoutComission=" + amountWithoutCommission
                + ", amountWithCommission=" + amountWithCommission
                + ", amountOfCommission=" + amountOfCommission + ", info="
                + info + ", comment=" + comment + "]";
    }

}
