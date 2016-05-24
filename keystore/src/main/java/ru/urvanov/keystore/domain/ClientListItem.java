package ru.urvanov.keystore.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class ClientListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8533093845107638813L;

    @Id
    @Column(name = "id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;

    @Column(name = "name_out")
    private String name;

    @Column(name = "client_type_out")
    private String clientType;

    @Column(name = "unique_id_out")
    private String uniqueId;

    @Column(name = "contact_person_name_out")
    private String contactPersonName;

    @Column(name = "contact_person_email_out")
    private String contactPersonEmail;

    @Column(name = "contact_person_phone_out")
    private String contactPersonPhone;

    @Column(name = "itn_out")
    private String itn;

    @Column(name = "iec_out")
    private String iec;

    @Column(name = "active_out")
    private String active;

    @Column(name = "client_group_name_out")
    private String clientGroupName;

    @Column(name = "juridical_person_name_out")
    private String juridicalPersonName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getItn() {
        return itn;
    }

    public void setItn(String itn) {
        this.itn = itn;
    }

    public String getIec() {
        return iec;
    }

    public void setIec(String iec) {
        this.iec = iec;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getClientGroupName() {
        return clientGroupName;
    }

    public void setClientGroupName(String clientGroupName) {
        this.clientGroupName = clientGroupName;
    }

    public String getJuridicalPersonName() {
        return juridicalPersonName;
    }

    public void setJuridicalPersonName(String juridicalPersonName) {
        this.juridicalPersonName = juridicalPersonName;
    }

    @Override
    public String toString() {
        return "ClientListItem id=" + id + ", name=" + name;
    }
}
