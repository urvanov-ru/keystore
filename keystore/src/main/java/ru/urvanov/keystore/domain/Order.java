package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
@NamedQueries({ @NamedQuery(name = "findFullOrderById", query = "from Order o left outer join fetch o.payments p left outer join fetch o.keys where o.id=:id") })
public class Order implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8115578144535946373L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "createdat")
    private Date createdAt = new Date();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "createdby")
    private User createdBy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dict_service_type_id")
    private DictServiceType dictServiceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "pay_datetime")
    private Date payDateTime;

    @Column(name = "completed_datetime")
    private Date completedDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "key_activation_mode")
    private KeyActivationMode keyActivationMode;

    
    @Column(name = "payu_ref")
    private String payuRef;
    
    @Version
    @Column
    private Integer version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<Payment>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Key> keys = new HashSet<Key>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public DictServiceType getDictServiceType() {
        return dictServiceType;
    }

    public void setDictServiceType(DictServiceType dictServiceType) {
        this.dictServiceType = dictServiceType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    public KeyActivationMode getKeyActivationMode() {
        return keyActivationMode;
    }

    public void setKeyActivationMode(KeyActivationMode keyActivationMode) {
        this.keyActivationMode = keyActivationMode;
    }

    public String getPayuRef() {
        return payuRef;
    }

    public void setPayuRef(String payuRef) {
        this.payuRef = payuRef;
    }

    public Integer getVersion() {
        return version;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "Order id=" + id + ", createdAt=" + createdAt + ", createdBy="
                + createdBy;
    }

}
