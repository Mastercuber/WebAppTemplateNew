package org.avensio.common.spring;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.avensio.common.spring.util.Profiles;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;


@Component
@TestComponent
@Profile(value = {Profiles.DEVELOPMENT, Profiles.INTEGRATION_TEST})
@Slf4j
@PropertySource("classpath:application.properties")
public class StartupLoggingComponent implements InitializingBean {
    private static final String ENV_TARGET_KEY = "envTarget";
    private static final String PERSISTENCE_TARGET_KEY = "persistenceTarget";
    private static final String ACTIVE_SPRING_PROFILE_KEY = "spring.profiles.active";
    @Value("${spring.datasource.jdbc.url:jdbc.url}")
    private String PERSISTENCE_HOST_KEY;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

    public StartupLoggingComponent() {
        super();
    }

    //

    @Override
    public void afterPropertiesSet() {
        log.info("============================================================================");
        try {
            logEnvTarget(env);
            logPersistenceTarget(env);

            logActiveSpringProfile(env);
            logPersistenceData(env);
            Connection connection = dataSource.getConnection();
            log.info("Database url: {}", connection.getMetaData().getURL());
            log.info("Database MetaData: {}", connection.getMetaData());
            connection.close();

        } catch (final Exception ex) {
            log.warn("There was a problem logging data on startup", ex);
        }

        log.info("============================================================================");
    }

    // UTIL

    private void logEnvTarget(final Environment environment) {
        final String envTarget = getValueOfProperty(environment, ENV_TARGET_KEY, "dev", Lists.newArrayList("dev"));
        log.info("{} = {}", ENV_TARGET_KEY, envTarget);
    }

    private void logPersistenceTarget(final Environment environment) {
        final String envTarget = getValueOfProperty(environment, PERSISTENCE_TARGET_KEY, "mysql", Lists.newArrayList("h2", "mysql", "cargo"));
        log.info("{} = {}", PERSISTENCE_TARGET_KEY, envTarget);
    }

    private void logActiveSpringProfile(final Environment environment) {
        final String activeSpringProfile = getValueOfProperty(environment, ACTIVE_SPRING_PROFILE_KEY, "none", null);
        log.info("{} = {}", ACTIVE_SPRING_PROFILE_KEY, activeSpringProfile);
    }

    private void logPersistenceData(final Environment environment) {
        final String persistenceHost = getValueOfProperty(environment, PERSISTENCE_HOST_KEY, "not-found", null);
        log.info("{} = {}", PERSISTENCE_HOST_KEY, persistenceHost);
    }

    //

    private final String getValueOfProperty(final Environment environment, final String propertyKey, final String propertyDefaultValue, final List<String> acceptablePropertyValues) {
        String propValue = environment.getProperty(propertyKey);
        if (propValue == null) {
            propValue = propertyDefaultValue;
            log.info("The {} doesn't have an explicit value; default value is = {}", propertyKey, propertyDefaultValue);
        }

        if (acceptablePropertyValues != null) {
            if (!acceptablePropertyValues.contains(propValue)) {
                log.warn("The property = {} has an invalid value = {}", propertyKey, propValue);
            }
        }

        if (propValue == null) {
            log.warn("The property = {} is null", propertyKey);
        }

        return propValue;
    }

}
