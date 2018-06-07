package org.avensio.common.test.base;

//import io.restassured.module.mockmvc.RestAssuredMockMvc;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.avensio.common.persistence.model.security.Privilege;
import org.avensio.common.service.security.IPrivilegeService;
import org.avensio.common.service.security.IRoleService;
import org.avensio.common.test.application.UnitTestApplication;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.ArrayList;

/**
 * Use restassured without spring-mvc
 * <p>
 * Use restassured with spring-mvc
 */
//import static io.restassured.RestAssured.*;
/**
 * Use restassured with spring-mvc
 */

/**
 * Used when a test focuses only on Spring MVC components.
 * eg Enabled:
 * - @Controller, @ControllerAdvice, @WebMvcConfigurer, @HandlerMethodArgumentResolver
 * Disabled: (use @MockBean)
 * - @Component, @Service, @Repository
 *
 * --> auto-configure Spring Security and MockMvc (enables @MockBean)
 * support:
 * - HtmlUnit
 * - WebClient
 * - Selenium WebDriver
 *
 * @See <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html">WebMvcTest</a>
 *
 * For full application configuration + MockMVC use
 * - @SpringBootTest
 * - @@AutoConfigureMockMvc
 */
//@WebMvcTest(controllers = {RoleController.class, PrivilegeController.class})
@SpringBootTest(classes = UnitTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// more fine-grained control of MockMVC
@AutoConfigureMockMvc(addFilters = true, print = MockMvcPrint.DEFAULT, printOnlyOnFailure = true, secure = false, webClientEnabled = true, webDriverEnabled = true)
//@Profile({"unittest"})
@TestPropertySource(value = "classpath:application-unit-test.properties")
//@ActiveProfiles({"unittest"})
@Slf4j
public class UnitTestBase extends TestBase {
    protected MockMvc mockMvc;
    @Autowired
    protected GenericWebApplicationContext webApplicationContext;
    @MockBean
    protected IPrivilegeService privilegeService;
    @MockBean
    protected IRoleService roleService;

    @Before
    public void setUp() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RestAssuredMockMvc.mockMvc(mockMvc);

        ArrayList<Privilege> privileges = new ArrayList<>();
        Privilege can_show = new Privilege("CAN_SHOW");
        can_show.setDescription("Just a description!");
        privileges.add(can_show);

        Mockito.when(privilegeService.findAll()).thenReturn(privileges);
    }

    @Test
    public void test() {

    }
}
