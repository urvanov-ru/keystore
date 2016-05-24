package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "findFullUserById", query = "from User u left outer join fetch u.authorities a left outer join fetch u.userAccesses left outer join fetch u.linkUserDictEventNotifications where u.id = :id"),
        @NamedQuery(name = "findFullUserByUserName", query = "from User u left outer join fetch u.authorities a left outer join fetch u.userAccesses left outer join fetch u.linkUserDictEventNotifications where LOWER(u.userName) = LOWER(:userName)"),
        @NamedQuery(name = "findUserByUserName", query = "from User u where LOWER(u.userName) = LOWER(:userName)")})
public class User implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3913223344466562238L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String userName;

    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled;

    @Size(max = 1024)
    @Column(name = "fullname")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Size(max = 50)
    @Column
    private String phone;

    @Size(max = 50)
    @Column
    private String post;

    @Column
    private Date birthdate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Version
    @Column
    private Integer version;

    @Column(name = "activation_key")
    private String activationKey;

    @Column(name = "change_password_key")
    private String changePasswordKey;

    @NotNull
    @Column(name = "activated")
    private Boolean activated = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @MapKey(name = "code")
    private Map<UserAccessCode, UserAccess> userAccesses = new HashMap<UserAccessCode, UserAccess>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @MapKey(name = "dictEvent")
    private Map<DictEvent, LinkUserDictEventNotification> linkUserDictEventNotifications = new HashMap<DictEvent, LinkUserDictEventNotification>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getVersion() {
        return version;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Map<UserAccessCode, UserAccess> getUserAccesses() {
        return userAccesses;
    }

    public void setUserAccesses(Map<UserAccessCode, UserAccess> userAccesses) {
        this.userAccesses = userAccesses;
    }

    public Map<DictEvent, LinkUserDictEventNotification> getLinkUserDictEventNotifications() {
        return linkUserDictEventNotifications;
    }

    public void setLinkUserDictEventNotifications(
            Map<DictEvent, LinkUserDictEventNotification> linkUserDictEventNotifications) {
        this.linkUserDictEventNotifications = linkUserDictEventNotifications;
    }

    @Override
    public String toString() {
        return "User id=" + id + ", fullName=" + fullName + ", userName="
                + userName;
    }

    public void setEmail(String email) {
        this.userName = email;
    }

    public String getEmail() {
        return this.userName;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getChangePasswordKey() {
        return changePasswordKey;
    }

    public void setChangePasswordKey(String changePasswordKey) {
        this.changePasswordKey = changePasswordKey;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

}
