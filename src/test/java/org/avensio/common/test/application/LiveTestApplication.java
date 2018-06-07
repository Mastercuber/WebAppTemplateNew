package org.avensio.common.test.application;

import org.springframework.boot.SpringApplication;

//@Import(LiveTestConfiguration.class)
public class LiveTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationTestApplication.class, args);
    }
}
