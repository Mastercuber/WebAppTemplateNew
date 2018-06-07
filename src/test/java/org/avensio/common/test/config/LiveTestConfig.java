package org.avensio.common.test.config;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({
        /*IntegrationTestConfiguration.class*/
})
@Slf4j
// !!!!!!!!!!!!!!!!! DEPRECATED use LiveTestApplication to define beans etc..!!!!!!!!!!!!!!!!!!!
public class LiveTestConfig {
}
