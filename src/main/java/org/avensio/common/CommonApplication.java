package org.avensio.common;

import org.avensio.common.persistence.dao.jpa.security.IPrincipalJpaDao;
import org.avensio.common.persistence.model.security.Principal;
import org.avensio.common.spring.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
@Import({
        ContextConfig.class,
        WebMvcConfig.class,
        WebSecurityConfig.class,
        PersistenceJpaConfig.class,
        ServiceConfig.class,
        RedisConfig.class,
        WebSocketConfig.class
        //AuthorizationServerConfig.class
})
public class CommonApplication {
    @Autowired
    IPrincipalJpaDao principalJpaDao;

    @Bean
    public Converter<String, Principal> messageConverter() {
        return id -> principalJpaDao.findOne(Long.valueOf(id));
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CommonApplication.class, args);
    }

}
