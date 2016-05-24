package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class ReportPaymentData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2156254309106493478L;
    
    @Id
    @Column(name = "id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;
    
    @Column(name = "date_begin_out")
    private Date dateBegin;
    
    @Column(name = "date_end_out")
    private Date dateEnd;
    
    @Column(name = "dict_service_type_id_out")
    private Long dictServiceTypeId;
    
    @Column(name = "dict_service_type_name_out")
    private String dictServiceTypeName;
    
    @Column(name = "profit_out")
    private BigDecimal profit;
    
    @Column(name = "order_payment_amount_out")
    private BigDecimal orderPaymentAmount;
    
    @Column(name = "pay_back_amount_out")
    private BigDecimal payBackAmount;
    
    @Column(name = "active_keys_out")
    private Long activeKeys;
    
    @Column(name = "new_keys_out")
    private Long newKeys;
    
    @Column(name = "expired_keys_out")
    private Long expiredKeys;
    
    @Column(name = "canceled_keys_out")
    private Long canceledKeys;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getDateBegin() {
        return dateBegin;
    }
    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }
    public Date getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
    public Long getDictServiceTypeId() {
        return dictServiceTypeId;
    }
    public void setDictServiceTypeId(Long dictServiceTypeId) {
        this.dictServiceTypeId = dictServiceTypeId;
    }
    public String getDictServiceTypeName() {
        return dictServiceTypeName;
    }
    public void setDictServiceTypeName(String dictServiceTypeName) {
        this.dictServiceTypeName = dictServiceTypeName;
    }
    public BigDecimal getProfit() {
        return profit;
    }
    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
    public BigDecimal getOrderPaymentAmount() {
        return orderPaymentAmount;
    }
    public void setOrderPaymentAmount(BigDecimal orderPaymentAmount) {
        this.orderPaymentAmount = orderPaymentAmount;
    }
    public BigDecimal getPayBackAmount() {
        return payBackAmount;
    }
    public void setPayBackAmount(BigDecimal payBackAmount) {
        this.payBackAmount = payBackAmount;
    }
    public Long getActiveKeys() {
        return activeKeys;
    }
    public void setActiveKeys(Long activeKeys) {
        this.activeKeys = activeKeys;
    }
    public Long getNewKeys() {
        return newKeys;
    }
    public void setNewKeys(Long newKeys) {
        this.newKeys = newKeys;
    }
    public Long getExpiredKeys() {
        return expiredKeys;
    }
    public void setExpiredKeys(Long expiredKeys) {
        this.expiredKeys = expiredKeys;
    }
    public Long getCanceledKeys() {
        return canceledKeys;
    }
    public void setCanceledKeys(Long canceledKeys) {
        this.canceledKeys = canceledKeys;
    }
    @Override
    public String toString() {
        return "ReportPaymentData [id=" + id + ", dateBegin=" + dateBegin
                + ", dateEnd=" + dateEnd + ", dictServiceTypeId="
                + dictServiceTypeId + ", DictServiceTypeName="
                + dictServiceTypeName + ", profit=" + profit
                + ", orderPaymentAmount=" + orderPaymentAmount
                + ", payBackAmount=" + payBackAmount + ", activeKeys="
                + activeKeys + ", newKeys=" + newKeys + ", expiredKeys="
                + expiredKeys + ", canceledKeys=" + canceledKeys + "]";
    }
    

}
