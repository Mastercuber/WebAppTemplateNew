package org.avensio.common.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
/**
 * - creates springSessionRepositoryFilter bean
 * -> Is what is in charge of replacing the HttpSession with
 * -> an implementation backed by spring
 */
//@EnableRedisHttpSession
@Profile("redis-http-session")
public class SessionConfig /*AbstractHttpSessionApplicationInitializer*/ {
}
