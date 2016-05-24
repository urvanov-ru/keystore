package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dict_client_group")
public class DictClientGroup implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8371959703154607432L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "dict_client_group_id")
    private DictClientGroup dictClientGroup;

    @Version
    @Column
    private int version;

    @OneToMany(mappedBy = "dictClientGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DictClientGroup> childDictClientGroups = new HashSet<DictClientGroup>();

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

    public DictClientGroup getDictClientGroup() {
        return dictClientGroup;
    }

    public void setDictClientGroup(DictClientGroup dictClientGroup) {
        this.dictClientGroup = dictClientGroup;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set<DictClientGroup> getChildDictClientGroups() {
        return childDictClientGroups;
    }

    public void setChildDictClientGroups(
            Set<DictClientGroup> childDictClientGroups) {
        this.childDictClientGroups = childDictClientGroups;
    }

    @Override
    public String toString() {
        return "DictClientGroup id = " + id + ", name = " + name + ". ";
    }
}
