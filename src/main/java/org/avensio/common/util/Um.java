package org.avensio.common.util;

public final class Um {

    /**
     * Privileges <br/>
     * - note: the fact that these Privileges are prefixed with `ROLES` is a Spring convention (which can be overriden if needed)
     */
    public static final String ARMIN_USER = "armin";
    public static final String ARMIN_PASS = "$2a$10$MJbuZPopRNIS6f.Y9H0DL.YNicYGpRG0cMh.uFyyBLglM61ODabGO";
    public static final String ARMIN_EMAIL = "kuar1013@hs-karlsruhe.de";

    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASS = "$2a$10$tgU1o3zyeY4zCXR5uVskaeE4Mq5VcSNt3WlpZGR.KUsqyzvyhEDoq";
    public static final String ADMIN_EMAIL = "admin@hs-karlsruhe.de";

    public static final String ZIRPINS_USER = "zirpins";
    public static final String ZIRPINS_PASS = "$2a$10$cF2m/YjCyt4TDVI/rz.HXOJziMDawWurN8bwiq3mENSGjBXZwDm5e";
    public static final String ZIRPINS_EMAIL = "christian.zirpins@hs-karlsruhe.de";

    public static final String KNAPPERT_USER = "knappert";
    public static final String KNAPPERT_PASS = "$2a$10$MPY2vsPkBhsj3uWa2ogsG.lX65Q/TxaIH58y2s4saq0lt1wPSnDU2";
    public static final String KNAPPERT_EMAIL = "lothar.knappert@hs-karlsruhe.de";

    public static final String NICK_USER = "nick";
    public static final String NICK_PASS = "$2a$10$a/iKzyU1GLe8hirmW6McAOm5AyeUWhYQK6lfKWuHIqZ6cr6q.hcwi";
    public static final String NICK_EMAIL = "krni1019@hs-karlsruhe.de";

    // privileges

    public static final class Privileges {

        // TwitterUser
        public static final String CAN_USER_READ = "ROLE_USER_READ";
        public static final String CAN_USER_WRITE = "ROLE_USER_WRITE";

        // Role
        public static final String CAN_ROLE_READ = "ROLE_ROLE_READ";
        public static final String CAN_ROLE_WRITE = "ROLE_ROLE_WRITE";

        // Privilege
        public static final String CAN_PRIVILEGE_READ = "ROLE_PRIVILEGE_READ";
        public static final String CAN_PRIVILEGE_WRITE = "ROLE_PRIVILEGE_WRITE";


        // End user privileges

    }

    public static final class Roles {

        /** A placeholder role for administrator. */
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        /** A placeholder role for enduser. */
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ACTUATOR = "ACTUATOR";

    }

    private Um() {
        throw new AssertionError();
    }

}
