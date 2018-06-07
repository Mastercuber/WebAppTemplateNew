package org.avensio.common.test.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.avensio.common.spring.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import redis.clients.jedis.JedisShardInfo;

import java.util.concurrent.TimeUnit;

//@Import(IntegrationTestConfiguration.class)
@Slf4j
public class IntegrationTestApplication {
    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(){
            @Override
            public TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatEmbeddedServletContainer(tomcat);
            }
            @Override
            public void postProcessContext(Context context) {

                //   - Adding a JNDI-Ressource to the embedded Context

                /*ContextResource resource = new ContextResource();
                resource.setName("jdbc/mysql/kunkel24");
                resource.setType(DataSource.class.getName());
                resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
                resource.setProperty("url", "jdbc:mysql://kunkel24.de/message");
                resource.setProperty("username", "USER");
                resource.setProperty("password", "PASS");

                //- Adding a JNDI-RessourceLink to the embedded Context

                ContextResourceLink contextResourceLink = new ContextResourceLink();
                contextResourceLink.setName("jdbc/mysql/kunkel24");
                contextResourceLink.setType(DataSource.class.getName());
                contextResourceLink.setProperty("driverClassName", "com.mysql.jdbc.Driver");
                contextResourceLink.setProperty("url", "jdbc:mysql://kunkel24.de/message");
                contextResourceLink.setProperty("username", "root");
                contextResourceLink.setProperty("password", "********");

                context.getNamingResources().addResource(resource);*/
                //context.getNamingResources().addResourceLink(contextResourceLink);
            }
        };
        String port = environment.getProperty("tomcat.http.port"); //

        if (port == null || port.isEmpty())
            port = "8080";

        log.info("~~~ Integrationtest configuration --> port: {}", Integer.parseInt(port));
        ApplicationContext.setPort(Integer.parseInt(port));
        factory.setPort(ApplicationContext.getPort());
        factory.setSessionTimeout(5, TimeUnit.MINUTES);

        return factory;
    }
}
