package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class UserListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7481227304849447655L;

    @Id
    @Column(name = "id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;

    @Column(name = "username_out")
    private String userName;

    @Column(name = "fullname_out")
    private String fullName;

    @Column(name = "sex_name_out")
    private String sexName;

    @Column(name = "phone_out")
    private String phone;

    @Column(name = "post_out")
    private String post;

    @Column(name = "birthdate_out")
    private Date birthDate;

    @Column(name = "client_name_out")
    private String clientName;

    @Column(name = "enabled_out")
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserListItem id=" + id + ", userName=" + userName
                + ", fullName=" + fullName;
    }
}
