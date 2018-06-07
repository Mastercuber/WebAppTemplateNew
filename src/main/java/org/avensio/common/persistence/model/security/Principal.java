package org.avensio.common.persistence.model.security;

import lombok.extern.java.Log;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.avensio.common.persistence.INameableDto;
import org.avensio.common.persistence.model.INameableEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Log
public class Principal implements INameableEntity, INameableDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRINCIPAL_ID")
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true)
    private String name;
    @Column(name = "PRINCIPAL_EMAIL")
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "created")
    private Timestamp created;
    @Column(name ="enabled")
    private Boolean enabled;
    @Column( /* nullable = false */)
    private Boolean locked;
    @Column(name = "expired")
    private Boolean expired;

    // @formatter:off
    @ManyToMany( /* cascade = { CascadeType.REMOVE }, */fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "PRINCIPAL_ID", referencedColumnName = "PRINCIPAL_ID")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")})
    private Set<Role> roles;

    // @formatter:on

    public Principal() {
        super();
        this.enabled = Boolean.TRUE;
        this.locked = Boolean.FALSE;
    }

    public Principal(final String nameToSet, final String emailToSet, final String passwordToSet, final Set<Role> rolesToSet) {

        username = nameToSet;
        name = nameToSet;
        email = emailToSet;
        password = passwordToSet;
        roles = rolesToSet;
        this.enabled = Boolean.TRUE;
        this.locked = Boolean.FALSE;
        this.expired = Boolean.FALSE;
    }


    // API
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long idToSet) {
        id = idToSet;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(final String nameToSet) {
        this.name = nameToSet;
    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(final String nameToSet) {
        this.username = nameToSet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() { return email; }

    public void setEmail(final String emailToSet) { email = emailToSet; }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String passwordToSet) {
        password = passwordToSet;
    }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(final Set<Role> rolesToSet) {
        roles = rolesToSet;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(final Boolean lockedToSet) {
        locked = lockedToSet;
    }

    public String getCreated() {
        return created.toLocalDateTime().toString();
    }

    public void setCreated(Timestamp timestamp) { this.created = timestamp; }

    public boolean isLocked() { log.info("---->locked: " + locked);return locked.booleanValue(); }

    public boolean isEnabled() { return enabled.booleanValue(); }

    public void setEnabled(final boolean enabled) {
        this.enabled = Boolean.valueOf(enabled);
    }

    public boolean isExpired() { return expired.booleanValue(); }

    public void setExpired(final boolean expired) {
        this.expired = Boolean.valueOf(expired);
    }

    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;

        return new EqualsBuilder()
                .append(id, principal.id)
                .append(username, principal.username)
                .append(name, principal.name)
                .append(email, principal.email)
                .append(password, principal.password)
                .append(roles, principal.roles)
                .append(locked, principal.locked)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .append(name)
                .append(email)
                .append(password)
                .append(locked)
                .append(roles)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("username", username)
                .append("name", name)
                .append("email", email)
                .append("password", password)
                .append("locked", locked)
                .append("roles", roles)
                .toString();
    }
}
