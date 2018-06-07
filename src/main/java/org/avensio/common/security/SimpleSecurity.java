package org.avensio.common.security;

import org.springframework.core.NamedThreadLocal;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class SimpleSecurity {
    public static RedisTemplate<String, Object> template;

    private static final ThreadLocal<UserInfo> localThread = new NamedThreadLocal<UserInfo>("localThread-info");
    private static class UserInfo {
        public static String name;
    }

    public static void setLocalThread(String name) {
        UserInfo userInfo = new UserInfo();
        userInfo.name = name;
        localThread.set(userInfo);
    }

    public static void unsetUser() {
        localThread.set(null);}

    public static boolean isUserSignedIn(String name) {
        BoundHashOperations hashOps = template.boundHashOps("uid:" + name + ":auth");
        String auth = (String)hashOps.get("auth");

        if(auth == null)
            return false;

        return true;
    }

    public static boolean isSignedIn() {
        UserInfo userInfo = localThread.get();
        if(userInfo == null) {
            return false;
        }

        return true;
    }

    public static String getName() {
        return new String(localThread.get().name);
    }
}
