package org.avensio.common.spring.config;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan( basePackages = { "org.avensio.common.persistence" })
@PropertySource({ "classpath:application.properties" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "org.avensio.common.persistence.dao" })
@EntityScan( basePackages = { "org.avensio.common.persistence.model", "org.avensio.common.model" })
@Log
public class PersistenceJpaConfig {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private Environment env;
    @Autowired
    private DataSource dataSource;

    public PersistenceJpaConfig() {
        super();
    }

    /*@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[] { "org.avensio.common.persistence" });
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }


    @Bean
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }*/

    /*@Bean(name = "transactionManager")
    public DataSourceTransactionManager getTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    //

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "create-drop"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));

        // setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        // setProperty("hibernate.ejb.naming_strategy", org.hibernate.cfg.ImprovedNamingStrategy.class.getName());
        return hibernateProperties;
    }*/

}

