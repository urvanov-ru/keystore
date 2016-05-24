package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 281509102278466287L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "createdat")
    private Date createdAt =  new Date();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    @NotNull
    @Column(name = "amount_without_commission")
    private BigDecimal amountWithoutCommission;

    @NotNull
    @Column(name = "amount_with_commission")
    private BigDecimal amountWithCommission;

    @NotNull
    @Column(name = "amount_of_commission")
    private BigDecimal amountOfCommission;

    @Column
    private String info;

    @Column
    private String comment;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "document")
    private byte[] document;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Version
    @Column
    private Integer version;

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public BigDecimal getAmountWithoutCommission() {
        return amountWithoutCommission;
    }

    public void setAmountWithoutCommission(BigDecimal amountWithoutCommission) {
        this.amountWithoutCommission = amountWithoutCommission;
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

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Integer getVersion() {
        return version;
    }

}
