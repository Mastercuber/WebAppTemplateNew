package org.avensio.common.spring.config;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@Profile({"persistence"})
@PropertySource(value = "application.properties")
@EnableTransactionManagement
@Log
public class PersistenceConfig {
    @Autowired
    private Environment env;
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean(name = "h2DataSource")
    @Primary
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getProperty("spring.datasource.h2.driver-class-name"));
        driverManagerDataSource.setUrl(env.getProperty("spring.datasource.h2.url"));
        driverManagerDataSource.setUsername(env.getProperty("spring.datasource.h2.user"));
        driverManagerDataSource.setPassword(env.getProperty("spring.datasource.h2.password"));
        return driverManagerDataSource;
    }

    @Bean(name = "dataSource")
    public DataSource jdbcDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getProperty("spring.datasource.jdbc.driver-class-name"));
        driverManagerDataSource.setUrl(env.getProperty("spring.datasource.jdbc.url"));
        driverManagerDataSource.setUsername(env.getProperty("spring.datasource.jdbc.user"));
        driverManagerDataSource.setPassword(env.getProperty("spring.datasource.jdbc.password"));
        return driverManagerDataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(jdbcDataSource());
    }

    /*@Bean
    public SpringLiquibase liquibase() {

        // Locate change log file
        String changelogFile = env.getProperty("liquibase.change-log");
        Resource resource = resourceLoader.getResource(changelogFile);
        Assert.state(resource.exists(), "Unable to find file: " + changelogFile);

        // Configure Liquibase
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changelogFile);
        liquibase.setContexts("webmailer,dev,prod");
        liquibase.setDataSource(dataSource());
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(true);

        // Verbose logging
        Map<String, String> params = new HashMap<>();
        params.put("verbose", String.valueOf(true));
        liquibase.setChangeLogParameters(params);

        return liquibase;
    }*/
}