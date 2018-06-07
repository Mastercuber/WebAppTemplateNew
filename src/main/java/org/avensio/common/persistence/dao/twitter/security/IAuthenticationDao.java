package org.avensio.common.persistence.dao.twitter.security;

import java.util.concurrent.TimeUnit;

public interface IAuthenticationDao {
    boolean auth(String uname, String pass);
    String addAuth(String uname, long timeout, TimeUnit tUnit);
    String getAuthByUsername(String uname);
    String getUsernameByAuth(String auth);
    void deleteAuth(String uname);
}
