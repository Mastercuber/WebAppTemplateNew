package org.avensio.common.spring.config;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Created by Voldemord on 05.11.2016.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan("org.avensio.common.security")
@Profile("security-standalone")
@Log
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowBackSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true);
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(httpFirewall());
        web.debug(true);
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CookieCsrfTokenRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/signup",
                        "/user/register",
                        "/registrationConfirm*",
                        "/badUser*",
                        "/forgotPassword*",
                        "/user/resetPassword*",
                        "/user/changePassword*",
                        "/user/savePassword*",
                        "/js/**", "/css/**", "/img/**",
                        "/auth/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
            .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .loginProcessingUrl("/doLogin")
            .and()
                .logout().permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/doLogout", "GET"))
                    .deleteCookies("JESSIONID")
                    .deleteCookies("XSRF-TOKEN")
                    .invalidateHttpSession(true)
            .and()
                .csrf().csrfTokenRepository(csrfTokenRepository())
            .and()
                .cors()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                /*.enableSessionUrlRewriting(true)
                .invalidSessionUrl("/sessionInvalid.html")
                .sessionAuthenticationErrorUrl("/login?error=error&errorSession=errorSession")
                .sessionFixation().migrateSession()
                .maximumSessions(2)
                .expiredUrl("/sessionExpired.html");*/

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
        /*auth.authenticationProvider(daoAuthenticationProvider());*//*
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER", "ADMIN");*/
        /*auth.jdbcAuthentication()
                .dataSource(dataSource).passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(
                        "select username,password,enabled,locked from principal where username=?")
                .authoritiesByUsernameQuery(
                        "select username, role from principal_role where username =?");*/
    }
}
