package org.avensio.common.spring.config;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.sql.DataSource;

/*
~~ Load dynamically a properties file
@PropertySource({
        "classpath:persistence-${envTarget:mysql}.properties"
})*/
@Configuration
@ComponentScan( basePackages = {"org.avensio.common"})
@EnableAspectJAutoProxy // enable to use @Aspect
@Log
public class ContextConfig {
    public static JndiTemplate jndiTemplate;

    public ContextConfig() {
        super();
    }

    // beans
    //@Bean
    public JndiObjectFactoryBean jndiObjectFactoryBean() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setLookupOnStartup(false);
        jndiTemplate = new JndiTemplate();
        jndiObjectFactoryBean.setJndiName("java:comp/env/jdbc/mysql/kunkel24");
        jndiObjectFactoryBean.setJndiTemplate(jndiTemplate);
        jndiObjectFactoryBean.setExpectedType(DataSource.class);
        return jndiObjectFactoryBean;
    }

    /**
     * Bean must be explicitly defined to get the property resolution mechanism working
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        return pspc;
    }

}
