package org.avensio.common.test.integration;

import lombok.extern.java.Log;
import org.avensio.common.persistence.model.security.Privilege;
import org.avensio.common.spring.context.ApplicationContext;
import org.avensio.common.test.base.IntegrationTestBase;
import org.avensio.common.util.Um;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;

@Log
public class CommonIntegrationTest extends IntegrationTestBase {
    private String ROLES_URI;
    private String PRIVILEGES_URI;
    private String PRINCIPALS_URI;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        ROLES_URI = "http://localhost:" + ApplicationContext.getPort() + "/roles";
        PRIVILEGES_URI = "http://localhost:" + ApplicationContext.getPort() + "/privileges";
    }
    @Test
    public void restTemplateUsageTest() {
        restTemplate = ApplicationContext.getRestTemplate();
        ResponseEntity<Integer> forEntity = restTemplate.getForEntity(ROLES_URI + "/count", Integer.class);
        log.info("~~~ Role count: " + forEntity.getBody().intValue());
        Assert.assertEquals(2, forEntity.getBody().intValue());
    }
    @Test
    public void restAssuredUsageTest() {
        createPrivilege();
        log.info("~~~ Privilege created!");
    }
   /* @Test
    public void entityManagerUsageTest() {
        log.info("~~~ Privilege name: " + entityManager.find(Privilege.class, 1L).getName());
        Assert.assertTrue(entityManager.getEntityManager().isOpen());
    }*/
    @Test
    public void testH2Connection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        String dbUrl = "jdbc:h2:mem:sa/sa;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        Connection connection = DriverManager.getConnection(dbUrl, "sa", "sa");

        Assert.assertFalse(connection.isClosed());
        log.info("~~~ Database Meta Data: " + connection.getMetaData().toString());
        connection.close();
    }

    @Test
    public void testRedisConnection() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Assert.assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    public void securitySetupSuccessfullyDoneTest() {
        // Roles exist test
        given()
        .when()
            .get(ROLES_URI)
        .then()
            .statusCode(200)
            .body(containsString(Um.Roles.ROLE_ADMIN))
            .body(containsString(Um.Roles.ROLE_USER));

         if (debug) {
             log.info("~~~ Print roles: ");
             get(ROLES_URI).getBody().prettyPrint();
         }

        // Privilege exist test
        given()
        .when()
            .get(PRIVILEGES_URI)
        .then()
            .statusCode(200)
            .body(containsString(Um.Privileges.CAN_PRIVILEGE_READ))
            .body(containsString(Um.Privileges.CAN_PRIVILEGE_WRITE))
            .body(containsString(Um.Privileges.CAN_ROLE_READ))
            .body(containsString(Um.Privileges.CAN_ROLE_WRITE))
            .body(containsString(Um.Privileges.CAN_USER_READ))
            .body(containsString(Um.Privileges.CAN_USER_WRITE));

        if (debug) {
            log.info("~~~ Print privileges: ");
            get(PRIVILEGES_URI).getBody().prettyPrint();
        }
    }

    private void createPrivilege() {
        createPrivilege("CAN_SAVE");
    }

    private void createPrivilege(String privilegeName) {
        // create privilege
        given()
            .contentType("application/json")
            .body(new Privilege(privilegeName))
        .when()
            .post(PRIVILEGES_URI)
        .then()
            .statusCode(201);
    }
}
