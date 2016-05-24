package ru.urvanov.keystore.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dict_event")
public class DictEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6133821727510038295L;

    @Id
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private DictEventCode code;

    @NotNull
    @Size(min = 1, max = 1024)
    @Column
    private String title;

    @NotNull
    @Size(min = 1)
    @Column
    private String body;

    @Version
    @Column
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictEventCode getCode() {
        return code;
    }

    public void setCode(DictEventCode code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DictEvent) {
            DictEvent other = (DictEvent) obj;
            if (id != null && other.getId() != null)
                return id.compareTo(other.getId()) == 0;
            else if (id == null && other.getId() != null || id != null
                    && other.getId() == null)
                return false;
            else
                return super.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "DictEvent id=" + id + ", code=" + code + ". ";
    }

}
