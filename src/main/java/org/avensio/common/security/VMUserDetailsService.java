package org.avensio.common.security;

import org.avensio.common.persistence.dao.jpa.security.IPrincipalJpaDao;
import org.avensio.common.persistence.model.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class VMUserDetailsService implements UserDetailsService {
    @Autowired
    private IPrincipalJpaDao principalJpaDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Principal principal = principalJpaDao.findByUsername(username);
        if ( principal== null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(principal.getEmail(), principal.getPassword(), true, true, true, true, getAuthorities(new String[]{"ROLE_USER", "ROLE_ADMIN"}));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String[] roles) {
        List<SimpleGrantedAuthority> authorities = Arrays.asList();
        for (String role: roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
