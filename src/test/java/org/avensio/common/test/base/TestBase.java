package org.avensio.common.test.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Is used to provide a bridge between Spring Boot test features and JUnit
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = "classpath:application-test-base.properties")
public class TestBase implements ServletContextAware, ApplicationContextAware {
    @Value("${testInt}")
    protected int testInit;
    @Value("${testKey}")
    protected String testKey;
    @Value("${debug:false}")
    protected boolean debug;
    @Autowired
    protected Environment env;
    @Autowired
    protected ResourceLoader resourceLoader;
    @Autowired(required = false)
    protected ObjectMapper objectMapper;

    protected ServletContext servletContext;
    protected ApplicationContext applicationContext;

    public TestBase() {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
