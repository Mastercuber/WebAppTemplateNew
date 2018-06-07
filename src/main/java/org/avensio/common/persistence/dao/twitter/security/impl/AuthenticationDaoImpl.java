package org.avensio.common.persistence.dao.twitter.security.impl;

import org.avensio.common.persistence.dao.twitter.security.IAuthenticationDao;
import org.avensio.common.persistence.dao.twitter.security.IUserDao;
import org.avensio.common.persistence.model.security.twitter.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class AuthenticationDaoImpl implements IAuthenticationDao {
    @Autowired
    private RedisTemplate<String, Object> template;
    @Autowired
    private IUserDao repo;

    // Login
    @Override
    public boolean auth(final String uname, final String pass) {
        final TwitterUser user = repo.getUser(uname);

        return user != null ? user.getPassword().equals(pass) : false;
    }

    // Add Cookie Value to Redis .. (beim Login)
    @Override
    public String addAuth(final String uname, final long timeout, final TimeUnit tUnit) {
        final TwitterUser user = repo.getUser(uname);
        final String auth = UUID.randomUUID().toString();

        template.boundHashOps("uid:" + user.getUsername() + ":auth").put("auth", auth);
        template.expire("uid:" + user.getUsername() + ":auth", timeout, tUnit);
        template.opsForValue().set("auth:" + auth + ":uid", user.getUsername(), timeout, tUnit);

        return auth;
    }

    @Override
    public String getAuthByUsername(final String uname) {
        return (String)template.opsForHash().get("uid:" + uname + ":auth", "auth");
    }

    @Override
    public String getUsernameByAuth(final String auth) {
        return (String)template.opsForValue().get("auth:" + auth + ":uid");
    }

    // Delete Cookie Value from Redis... (beim Logout)
    @Override
    public void deleteAuth(String uname) {
        String authKey = "uid:" + uname + ":auth";
        String auth = (String) template.boundHashOps(authKey).get("auth");
        ArrayList<String> keysToDelete = new ArrayList<>(Arrays.asList(authKey, "auth:"+auth+":uid"));
        template.delete(keysToDelete);
    }
}