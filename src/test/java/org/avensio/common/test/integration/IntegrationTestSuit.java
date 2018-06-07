package org.avensio.common.test.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.TestComponent;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommonIntegrationTest.class,
        PrivilegeControllerIntegrationTest.class,
        RoleControllerIntegrationTest.class
})
@TestComponent
public class IntegrationTestSuit {
    //
}
