package org.avensio.common.test.integration;

import lombok.extern.slf4j.Slf4j;
import org.avensio.common.persistence.model.security.Privilege;
import org.avensio.common.persistence.model.security.Role;
import org.avensio.common.spring.context.ApplicationContext;
import org.avensio.common.test.base.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@Slf4j
public class RoleControllerIntegrationTest extends IntegrationTestBase {
    private HashSet<Privilege> privilegesSet;
    private String ROLES_URI;
    private String PRIVILEGES_URI;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        ROLES_URI = "http://localhost:" + ApplicationContext.getPort() + "/roles";
        PRIVILEGES_URI = "http://localhost:" + ApplicationContext.getPort() + "/privileges";
        Privilege can_read = new Privilege("CAN_READ");
        Privilege can_write = new Privilege("CAN_WRITE");

        given()
                .body(can_read)
                .contentType("application/json")
                .when()
                .post(PRIVILEGES_URI)
                .then()
                .statusCode(201);

        given()
                .body(can_write)
                .contentType("application/json")
                .when()
                .post(PRIVILEGES_URI)
                .then()
                .statusCode(201);

        log.info("~~~ Before ends!");
    }

    @Test
    public void createRoleTest() {
        Integer count = get("/roles/count").getBody().as(Integer.class);

        createRole();

        Assert.assertTrue(get("/roles/count").getBody().as(Integer.class).intValue() == (count.intValue() + 1));
        log.info("~~~ Created role! ~~~~~~~~~~~");
    }

    @Test
    public void createPrivilegeTest() {
        Privilege privilege = new Privilege();
        privilege.setName("CAN_READ_TEST");
        privilege.setDescription("Test Privilege");
        given()
                .body(privilege)
                .contentType("application/json")
                .when()
                .post("/privileges")
                .then()
                .statusCode(201);

        log.info("~~~ Create privilege!");
    }

    private void createRole() {
        createRole("ADMIN");
    }

    private void createRole(String roleName) {
        Role role = new Role(roleName, privilegesSet);

        // create role
        given()
            .contentType("application/json")
            .body(role)
        .when()
            .post(ROLES_URI)
        .then()
            .statusCode(201);
    }
}
