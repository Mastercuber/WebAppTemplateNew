package org.avensio.common.spring.util;

/**
 * Created by Voldemord on 03.11.2016.
 */
public final class Profiles {

    public static final String DEPLOYED = "deployed";
    public static final String DEVELOPMENT = "dev";
    public static final String INTEGRATION_TEST = "integration-test";

    private Profiles() {
        throw new AssertionError();
    }

}

