package org.avensio.common.test.application;

import org.springframework.boot.SpringApplication;

//@Import(UnitTestConfiguration.class)
public class UnitTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationTestApplication.class, args);
    }
}
