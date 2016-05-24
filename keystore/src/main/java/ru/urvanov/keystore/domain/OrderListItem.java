package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class OrderListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6096320403373666513L;

    @Id
    @Column(name = "id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;

    @Column(name = "dict_service_type_name_out")
    private String dictServiceTypeName;

    @Column(name = "status_out")
    private String status;
    
    @Column(name = "status_name_out")
    private String statusName;

    @Column(name = "createdBy_name_out")
    private String createdByName;

    @Column(name = "createdAt_out")
    private Date createdAt;

    @Column(name = "pay_datetime_out")
    private Date payDateTime;

    @Column(name = "completed_datetime_out")
    private Date completedDateTime;

    @Column(name = "client_name_out")
    private String clientName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictServiceTypeName() {
        return dictServiceTypeName;
    }

    public void setDictServiceTypeName(String dictServiceTypeName) {
        this.dictServiceTypeName = dictServiceTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(Date payDateTime) {
        this.payDateTime = payDateTime;
    }

    public Date getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(Date completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    @Override
    public String toString() {
        return "OrderListItem id=" + id + ", dictServiceTypeName="
                + dictServiceTypeName + ", statusName=" + statusName + ".";
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
