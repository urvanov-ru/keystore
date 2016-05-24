package ru.urvanov.keystore.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "link_user_dict_event_notification")
public class LinkUserDictEventNotification implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3714997276207858000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dict_event_id")
    private DictEvent dictEvent;

    @NotNull
    @Column(name = "allow_notification")
    private Boolean allowNotification;

    @Version
    @Column
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DictEvent getDictEvent() {
        return dictEvent;
    }

    public void setDictEvent(DictEvent dictEvent) {
        this.dictEvent = dictEvent;
    }

    public Boolean getAllowNotification() {
        return allowNotification;
    }

    public void setAllowNotification(Boolean allowNotification) {
        this.allowNotification = allowNotification;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "LinkUserDictEventNotification id = " + id + ". ";
    }

}
