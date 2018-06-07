package org.avensio.common.test.config;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.avensio.common.spring.config.ContextConfig;
import org.avensio.common.spring.config.WebMvcConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Import({
        /*WebMvcConfig.class,
        ContextConfig.class*/
})
@Slf4j

// !!!!!!!!!!!!!!!!! DEPRECATED use UnitTestApplication to define beans etc..!!!!!!!!!!!!!!!!!!!
public class UnitTestConfig {
}
