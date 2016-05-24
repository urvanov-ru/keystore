package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dict_action")
public class DictAction implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5534046197638120352L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Column
    @NotNull
    @Size(min = 1, max = 1024)
    private String description;

    @Column(name = "date_begin")
    private Date dateBegin;

    @Column(name = "date_end")
    private Date dateEnd;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dict_service_type_id")
    private DictServiceType dictServiceType;

    @NotNull
    @Column(name = "for_new_clients")
    private Boolean forNewClients = false;

    @NotNull
    @Column(name = "dict_action_type")
    @Enumerated(EnumType.STRING)
    private DictActionType dictActionType;

    @Version
    @Column
    private Integer version;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public DictServiceType getDictServiceType() {
        return dictServiceType;
    }

    public void setDictServiceType(DictServiceType dictServiceType) {
        this.dictServiceType = dictServiceType;
    }

    public Boolean getForNewClients() {
        return forNewClients;
    }

    public void setForNewClients(Boolean forNewClients) {
        this.forNewClients = forNewClients;
    }

    public Integer getVersion() {
        return version;
    }

    public DictActionType getDictActionType() {
        return dictActionType;
    }

    public void setDictActionType(DictActionType dictActionType) {
        this.dictActionType = dictActionType;
    }
    
    @Override
    public int hashCode() {
        return id == null ? -1 : id.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof DictAction) {
            DictAction otherDictAction = (DictAction)other;
            if (otherDictAction.getId() != null && getId() != null &&
                    (otherDictAction.getId().equals(getId())))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "DictActionType id=" + id + ", name=" + name + ", description="
                + description;
    }
}
