package org.avensio.common.security;

import javax.transaction.Transactional;
import java.util.*;

import org.avensio.common.persistence.dao.jpa.security.IPrincipalJpaDao;
import org.avensio.common.persistence.dao.jpa.security.IRoleJpaDao;
import org.avensio.common.persistence.dao.jpa.security.IUserJpaDao;
import org.avensio.common.persistence.model.security.Principal;
import org.avensio.common.persistence.model.security.Role;
import org.avensio.common.persistence.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@Transactional
public class AvensioUserDetailsService implements UserDetailsService {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private IUserJpaDao userJpaDao;
    @Autowired
    private IRoleJpaDao roleJpaDao;
    @Autowired
    private IPrincipalJpaDao principalJpaDao;

    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = userJpaDao.findByEmail(email);
        final Principal principal = principalJpaDao.findByEmail(email);
        org.springframework.security.core.userdetails.User resultUser = null;
        List<Role> allRoles = roleJpaDao.findAll();

        if (user == null && principal == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }

        if (user != null) {
            resultUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getEnabled(), true, true, true, getAuthorities(ROLE_USER));
        }
        if (principal != null) {
            resultUser = new org.springframework.security.core.userdetails.User(principal.getEmail(), principal.getPassword(), principal.isEnabled(), true, true, true, getAuthorities(principal.getRoles()));
        }

        return resultUser;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

}