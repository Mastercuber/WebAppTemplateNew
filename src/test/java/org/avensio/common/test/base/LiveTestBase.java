package org.avensio.common.test.base;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.avensio.common.persistence.dao.jpa.security.IPrincipalJpaDao;
import org.avensio.common.persistence.dao.jpa.security.IPrivilegeJpaDao;
import org.avensio.common.persistence.dao.jpa.security.IRoleJpaDao;
import org.avensio.common.service.security.IPrivilegeService;
import org.avensio.common.service.security.IRoleService;
import org.avensio.common.spring.config.ContextConfig;
import org.avensio.common.spring.context.ApplicationContext;
import org.avensio.common.test.application.LiveTestApplication;
import org.avensio.common.test.config.LiveTestConfig;
import org.avensio.common.test.util.DebugUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// more fine-grained control of MockMVC
//@AutoConfigureMockMvc(addFilters = true, print = MockMvcPrint.DEFAULT, printOnlyOnFailure = true, secure = false, webClientEnabled = true, webDriverEnabled = true)
/**
 * - Configuring H2, an in-memory database
 * - Setting Hibernate, Spring Data, and the DataSource
 * - Performing an @EntityScan
 * - Turning on SQL logging
 *
 */
@DataJpaTest
@TestPropertySource(value = "classpath:application-live-test.properties")
@Slf4j
@AutoConfigurationPackage
@ContextConfiguration(locations = {"classpath:/test/BeanConfig.xml"}, classes = {ContextConfig.class, LiveTestApplication.class, LiveTestConfig.class})
//@EnableTransactionManagement
//@ActiveProfiles({"integration-test"})
public class LiveTestBase extends IntegrationTestBase {

    @Autowired
    protected IRoleJpaDao roleJpaDao;
    @Autowired
    protected IPrivilegeJpaDao privilegeJpaDao;
    @Autowired
    protected IPrincipalJpaDao principalJpaDao;

    @Autowired
    protected IPrivilegeService privilegeService;
    @Autowired
    protected IRoleService roleService;

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected RedisTemplate redisTemplate;
    /*@Autowired
    protected StringRedisTemplate stringRedisTemplate;*/
    @Autowired
    protected TestEntityManager entityManager;
    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    protected RestTemplate restTemplate;

    private static RedisServer redisServer;
    private static boolean isDone = false;
    @Value("${server.port}")
    private int port;

    @BeforeClass
    public static void seUpBeforeClass() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @Before
    public void setUp() {
        if (!isDone) {
            RestAssured.port = Integer.valueOf(this.port).intValue();
            RestAssured.baseURI ="http://127.0.0.1:" + this.port + "/wd/hub";

            ArrayList<Filter> filters = new ArrayList<>();
            filters.add(new RequestLoggingFilter());
            filters.add(new ResponseLoggingFilter());
            filters.add(new ErrorLoggingFilter());
            RestAssured.filters(filters);

            log.info("~~~~~~~~~~~~~ WebAppContext: {}", webApplicationContext);
            ApplicationContext.setWebApplicationContext(webApplicationContext);

            //RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
            mockMvc = MockMvcBuilders.webAppContextSetup(ApplicationContext.getWebApplicationContext()).build();
            //mockMvc = MockMvcBuilders.standaloneSetup(Arrays.array(RoleController.class, PrivilegeController.class)).build();
            RestAssuredMockMvc.mockMvc(this.mockMvc);
            ApplicationContext.setRestTemplate(restTemplate = new RestTemplate(new MockMvcClientHttpRequestFactory(mockMvc)));
            ApplicationContext.setJdbcTemplate(jdbcTemplate);
            //ApplicationContext.setEntityManager(entityManager);
            ApplicationContext.setRedisTemplate(redisTemplate);
            /*ApplicationContext.setStringRedisTemplate(stringRedisTemplate);*/
            ApplicationContext.environment = env;
            DebugUtil.logApplicationContext();
            //DebugUtil.logThread(Thread.currentThread());
            isDone = true;
        }
    }

    @AfterClass
    public static void tearDown() {
        redisServer.stop();
    }
}