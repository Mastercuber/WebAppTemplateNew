package org.avensio.common.persistence.dao.twitter.security.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.avensio.common.persistence.dao.twitter.security.IUserDao;
import org.avensio.common.persistence.model.security.twitter.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@Repository
public class TwitterUserDaoImpl implements IUserDao {

    @Autowired
    private RedisTemplate<String, Object> template;
    @Autowired
    private ObjectMapper mapper;

    /** TwitterUser Operations
     *
     */

    @Override
    public boolean addUser(TwitterUser twitterUser) {
        BoundHashOperations hashOps = template.boundHashOps(twitterUser.getUsername());

        // Möglichkeit 1 zum Zugriff auf TwitterUser
        hashOps.put("username", twitterUser.getUsername());
        hashOps.put("password", twitterUser.getPassword());
        hashOps.put("email", twitterUser.getEmail());

        // Speicher des neu Registrierten users in extra Struktur
        template.opsForSet().add("user", twitterUser.getUsername());

        // Möglichkeit 2 zum Zugriff auf TwitterUser
        try {
            template.opsForValue().set(twitterUser.getUsername(), mapper.writeValueAsString(twitterUser));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public TwitterUser getUser(String username) {
        TwitterUser tmp = null;
        try {
            String user = (String) template.opsForValue().get(username);
            if(user != null)
                tmp = mapper.readValue(user, TwitterUser.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tmp;
    }

    @Override
    public TwitterUser updateUser(String username) {
        return null;
    }

    @Override
    public boolean deleteUser(String username) {
        template.opsForValue().set(username, null);
        template.opsForSet().remove("user", username);

        BoundHashOperations hashOps = template.boundHashOps(username);
        hashOps.delete("username", "password", "email");
        return true;
    }

    @Override
    public ArrayList<String> searchUsers(String query) {
        Iterator usersIterator = template.opsForSet().members("user").iterator();
        ArrayList<String> result = new ArrayList<>();

        while(usersIterator.hasNext()) {
            String user = (String) usersIterator.next();
            user = user.toLowerCase();
            if (user.startsWith(query.toLowerCase())) {
                result.add(user);
            }
        }

        return result;
    }
}
