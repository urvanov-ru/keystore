package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class KeyListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4327060907256747159L;

    @Id
    @Column(name = "id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;

    @Column(name = "dict_service_type_name_out")
    private String dictServiceTypeName;

    @Column(name = "basis_out")
    private String basis;

    @Column(name = "status_out")
    private String status;

    @Column(name = "status_name_out")
    private String statusName;

    @Column(name = "date_begin_out")
    private Date dateBegin;

    @Column(name = "date_end_out")
    private Date dateEnd;
    
    @Column(name = "dict_action_id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long dictActionId;

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

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
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

    public Long getDictActionId() {
        return dictActionId;
    }

    public void setDictActionId(Long dictActionId) {
        this.dictActionId = dictActionId;
    }

    @Override
    public String toString() {
        return "KeyListItem [id=" + id + ", dictServiceTypeName="
                + dictServiceTypeName + ", basis=" + basis + ", status="
                + status + ", statusName=" + statusName + ", dateBegin="
                + dateBegin + ", dateEnd=" + dateEnd + ", dictActionId="
                + dictActionId + "]";
    }

    
}
