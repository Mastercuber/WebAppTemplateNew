package org.avensio.common.test.util;

import lombok.extern.slf4j.Slf4j;
import org.avensio.common.spring.context.ApplicationContext;

@Slf4j
public class DebugUtil {
    public static void logThread(Thread thread) {
        log.info("~~~Thread~~~ ID: {}", thread.getId());
        log.info("~~~Thread~~~ Name: {}", thread.getName());
        log.info("~~~Thread~~~ State: {}", thread.getState());
        log.info("~~~Thread~~~ Priority: {}", thread.getPriority());
        log.info("~~~Thread~~~ ThreadGroup: {}", thread.getThreadGroup());
    }
    public static void logApplicationContext() {
        log.info("~~~Application Context~~~ WebAppContext: {}", ApplicationContext.getWebApplicationContext());
        log.info("~~~Application Context~~~ ApplicationContext: {}", ApplicationContext.getApplicationContext());
        log.info("~~~Application Context~~~ JdbcTemplate: {}", ApplicationContext.getJdbcTemplate());
        log.info("~~~Application Context~~~ RestTemplate: {}", ApplicationContext.getRestTemplate());
        //log.info("~~~Application Context~~~ EntityManager: {}", ApplicationContext.getEntityManager());
        log.info("~~~Application Context~~~ environment: {}", ApplicationContext.getEnvironment());
        log.info("~~~Application Context~~~ port: {}", ApplicationContext.getPort());
    }
}
