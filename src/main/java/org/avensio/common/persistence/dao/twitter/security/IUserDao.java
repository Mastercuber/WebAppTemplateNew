package org.avensio.common.persistence.dao.twitter.security;

import org.avensio.common.persistence.model.security.twitter.TwitterUser;

import java.util.ArrayList;

public interface IUserDao {
    // twitterUser operations
    boolean addUser(TwitterUser user);
    TwitterUser getUser(String username);
    TwitterUser updateUser(String username);
    boolean deleteUser(String username);
    ArrayList<String> searchUsers(String query);
}
