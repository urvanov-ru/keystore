package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dict_service_type")
public class DictServiceType implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4712669646875664068L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    @Size(min=1, max=100)
    private String name;

    @NotNull
    @Column
    @Size(min=1, max=1024)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private DictServiceTypeStatus status;

    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull
    @Column(name = "amount_30_days")
    private BigDecimal amount30Days;

    @Column(name = "base_key")
    private String baseKey;

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

    public DictServiceTypeStatus getStatus() {
        return status;
    }

    public void setStatus(DictServiceTypeStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount30Days() {
        return amount30Days;
    }

    public void setAmount30Days(BigDecimal amount30Days) {
        this.amount30Days = amount30Days;
    }

    public Integer getVersion() {
        return version;
    }

    public String getBaseKey() {
        return baseKey;
    }

    public void setBaseKey(String baseKey) {
        this.baseKey = baseKey;
    }

    @Override
    public String toString() {
        return "DictServiceType id=" + id + ", name=" + name + ", amount="
                + amount + ", amount30Days=" + amount30Days;
    }
    
    @Override
    public int hashCode() {
        return id == null ? -1 : id.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof DictServiceType) {
            DictServiceType otherDictServiceType = (DictServiceType)other;
            if (this.getId() != null && otherDictServiceType.getId() != null) {
                return this.getId().equals(otherDictServiceType.getId());
            }
        }
        return false;
    }
}
