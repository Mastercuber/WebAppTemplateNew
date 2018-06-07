package org.avensio.common.test.integration;

import lombok.extern.slf4j.Slf4j;
import org.avensio.common.persistence.model.security.Privilege;
import org.avensio.common.spring.context.ApplicationContext;
import org.avensio.common.test.base.IntegrationTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@Slf4j
public class PrivilegeControllerIntegrationTest extends IntegrationTestBase {
    private String ROLES_URI;
    private String PRIVILEGES_URI;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        ROLES_URI = "http://localhost:" + ApplicationContext.getPort() + "/roles";
        PRIVILEGES_URI = "http://localhost:" + ApplicationContext.getPort() + "/privileges";
    }

    @Test
    public void createPrivilegeTest() {
        createPrivilege();
        log.info("~~~ Privilege created! Is debug enabled {}", log.isDebugEnabled());
        if (debug) {
            get(PRIVILEGES_URI).getBody().prettyPrint();
        }
    }

    private void createPrivilege() {
        createPrivilege("CAN_SAVE");
    }

    private void createPrivilege(String privilegeName) {
        Privilege privilege = new Privilege(privilegeName);

        // create role
        given()
            .contentType("application/json")
            .body(privilege)
        .when()
            .post(PRIVILEGES_URI)
        .then()
            .statusCode(201);
    }
}
