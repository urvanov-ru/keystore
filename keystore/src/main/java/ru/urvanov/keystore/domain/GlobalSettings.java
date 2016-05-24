package ru.urvanov.keystore.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "global_settings")
public class GlobalSettings implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4399245493008656696L;

    @Id
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "default_dict_client_group_id")
    private DictClientGroup defaultDictClientGroup;

    @NotNull
    @Column(name = "session_store_days")
    private Integer sessionStoreDays;

    @Version
    @Column
    private Integer version;

    @Override
    public String toString() {
        return "GlobalSettings id=" + id + ". ";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictClientGroup getDefaultDictClientGroup() {
        return defaultDictClientGroup;
    }

    public void setDefaultDictClientGroup(DictClientGroup defaultDictClientGroup) {
        this.defaultDictClientGroup = defaultDictClientGroup;
    }

    public Integer getSessionStoreDays() {
        return sessionStoreDays;
    }

    public void setSessionStoreDays(Integer sessionStoreDays) {
        this.sessionStoreDays = sessionStoreDays;
    }

    public Integer getVersion() {
        return version;
    }

}
