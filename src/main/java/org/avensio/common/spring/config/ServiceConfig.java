package org.avensio.common.spring.config;

import lombok.extern.java.Log;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan( basePackages = {"org.avensio.common.service.security.impl"})
@Log
public class ServiceConfig {

    public ServiceConfig() {
        super();
    }

    // beans

}
