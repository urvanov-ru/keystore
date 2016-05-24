package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "client")
@NamedQueries({ @NamedQuery(name = "findFullClientById", query = "from Client c left outer join fetch c.linkClientDictServiceTypes left outer join fetch c.linkClientDictActions where c.id=:id") })
public class Client implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5759477481149349645L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="createdAt")
    @NotNull
    private Date createdAt = new Date();
    
    @Column
    @Size(min = 1, max = 1024)
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    @NotNull
    private ClientType clientType;

    @Column(name = "unique_id")
    @Size(min = 1, max = 255)
    @NotNull
    private String uniqueId;

    @Column(name = "contact_person_name")
    @Size(min = 1, max = 255)
    @NotNull
    private String contactPersonName;

    @Column(name = "contact_person_email")
    @Size(min = 1, max = 50)
    @NotNull
    private String contactPersonEmail;

    @Size(min = 1, max = 50)
    @NotNull
    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Column(name = "itn")
    @Size(max = 12)
    private String itn;

    @Size(min = 9, max = 9)
    @Column(name = "iec")
    private String iec;

    @NotNull
    @Column(name = "active")
    private Boolean active;

    @Size(max = 1024)
    @Column(name = "juridical_person_name")
    private String juridicalPersonName;

    @Enumerated(EnumType.STRING)
    @Column(name = "key_activation_mode")
    @NotNull
    private KeyActivationMode keyActivationMode;

    @ManyToOne
    @JoinColumn(name = "dict_client_group_id")
    @NotNull
    private DictClientGroup dictClientGroup;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "link_client_dict_service_type", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "dict_service_type_id"))
    private Set<DictServiceType> linkClientDictServiceTypes = new HashSet<DictServiceType>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="link_client_dict_action", joinColumns = @JoinColumn(name="client_id"), inverseJoinColumns = @JoinColumn(name="dict_action_id"))
    private Set<DictAction> linkClientDictActions = new HashSet<DictAction>();
    
    @Version
    @Column
    private Integer version;

    @Column
    @Size(min = 1, max = 1024)
    @NotNull
    private String password;
    
    @Column(name="juridical_person_address")
    @Size(max=1024)
    private String juridicalPersonAddress;
    
    @Column(name="bank_name")
    @Size(max=1024)
    private String bankName;
    
    @Column(name="account")
    @Size(min=20, max=20)
    private String account;
    
    @Column(name="corr_account")
    @Size(min=20, max=20)
    private String corrAccount;
    
    @Column(name="bic")
    @Size(min=9, max=9)
    private String bic;
    
    @Column(name="authority")
    @NotNull
    private String authority;
    
    

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public KeyActivationMode getKeyActivationMode() {
        return keyActivationMode;
    }

    public void setKeyActivationMode(KeyActivationMode keyActivationMode) {
        this.keyActivationMode = keyActivationMode;
    }

    public DictClientGroup getDictClientGroup() {
        return dictClientGroup;
    }

    public void setDictClientGroup(DictClientGroup dictClientGroup) {
        this.dictClientGroup = dictClientGroup;
    }

    public Integer getVersion() {
        return version;
    }

    public Set<DictServiceType> getLinkClientDictServiceTypes() {
        return linkClientDictServiceTypes;
    }

    public void setLinkClientDictServiceTypes(
            Set<DictServiceType> linkClientDictServiceTypes) {
        this.linkClientDictServiceTypes = linkClientDictServiceTypes;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", createdAt=" + createdAt + ", name="
                + name + ", clientType=" + clientType + ", uniqueId="
                + uniqueId + ", contactPersonName=" + contactPersonName
                + ", contactPersonEmail=" + contactPersonEmail
                + ", contactPersonPhone=" + contactPersonPhone + ", itn=" + itn
                + ", iec=" + iec + ", active=" + active
                + ", juridicalPersonName=" + juridicalPersonName
                + ", keyActivationMode=" + keyActivationMode
                + ", dictClientGroup=" + dictClientGroup
                + ", linkClientDictServiceTypes=" + linkClientDictServiceTypes
                + ", linkClientDictActions=" + linkClientDictActions
                + ", version=" + version + ", password=" + password
                + ", juridicalPersonAddress=" + juridicalPersonAddress
                + ", bankName=" + bankName + ", account=" + account
                + ", corrAccount=" + corrAccount + ", bic=" + bic
                + ", authority=" + authority + "]";
    }

    public String getJuridicalPersonName() {
        return juridicalPersonName;
    }

    public void setJuridicalPersonName(String juridicalPersonName) {
        this.juridicalPersonName = juridicalPersonName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJuridicalPersonAddress() {
        return juridicalPersonAddress;
    }

    public void setJuridicalPersonAddress(String juridicalPersonAddress) {
        this.juridicalPersonAddress = juridicalPersonAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCorrAccount() {
        return corrAccount;
    }

    public void setCorrAccount(String corrAccount) {
        this.corrAccount = corrAccount;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<DictAction> getLinkClientDictActions() {
        return linkClientDictActions;
    }

    public void setLinkClientDictActions(Set<DictAction> linkClientDictActions) {
        this.linkClientDictActions = linkClientDictActions;
    }

}
