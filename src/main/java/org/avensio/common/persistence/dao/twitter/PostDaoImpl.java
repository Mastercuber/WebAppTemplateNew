package org.avensio.common.persistence.dao.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.avensio.common.persistence.model.twitter.Post;
import org.avensio.common.security.SimpleSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class PostDaoImpl implements IPostDao {

    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private ObjectMapper mapper;

    /** Post Operations
     *
     */
    @Override
    public Post addPost(String postText, String uname) {
        Post post = new Post();
        post.setPostText(postText);
        post.setUsername(uname);
        post.setDate(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString();
        post.setUuid(uuid);

        try {
            template.opsForValue().set(uuid, mapper.writeValueAsString(post));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Post();
        }

        template.opsForList().leftPush("postIds:" + uname, uuid);

        try {
            template.opsForList().leftPush("allPosts", mapper.writeValueAsString(post));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        template.convertAndSend("newPostsQueue", post.getUuid());

        return post;
    }

    @Override
    public ArrayList<String> getPostIds(String uname) {
        // Get all elements of list
        return (ArrayList)template.opsForList().range("postIds:" + uname, 0, -1);
    }

    @Override
    public Post getPost(String uuid) {
        try {
            return mapper.readValue(template.opsForValue().get(uuid).toString(), Post.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Post();
        }
    }

    @Override
    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        ListIterator<String> allPosts = template.opsForList().range("allPosts", 0, -1).listIterator();

        while(allPosts.hasNext()) {
            String postString = (String) allPosts.next();
            Post postObject = null;
            try {
                postObject = mapper.readValue(postString, Post.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            posts.add(postObject);
        }

        return posts;
    }

    @Override
    public ArrayList<Post> getAllPosts(String uname) {
        ArrayList<Post> posts = new ArrayList<>();
        ListIterator<String> following = template.opsForList().range("following:" + uname, 0, -1).listIterator();
        following.add(uname);

        do {
            for (String postId : getPostIds(uname)) {
                posts.add(getPost(postId));
            }
            uname = following.hasNext() ? following.next() : "";
        } while (following.hasNext());

        Collections.sort(posts, (o1, o2) -> (int) (o2.getDate() - o1.getDate()));

        return posts;
    }

    @Override
    public ArrayList<Post> getPaginatedPosts(String uname, int from, int to) {
        ArrayList<Post> posts;
        if(uname == null) {
            posts = getAllPosts();
        }else{
            posts = getAllPosts(uname);
        }

        if(posts.size() < to)
            to = posts.size();
        return new ArrayList<>(posts.subList(from, to));
    }

    /** Follower and Following Operations
     *
     */
    @Override
    public boolean follow(String uname) {
        template.opsForList().leftPush("follower:" + uname, SimpleSecurity.getName());
        template.opsForList().leftPush("following:" + SimpleSecurity.getName(), uname);

        return true;
    }

    @Override
    public boolean unfollow(String uname) {
        template.opsForList().remove("follower:" + uname, 0, SimpleSecurity.getName());
        template.opsForList().remove("following:" + SimpleSecurity.getName(), 0, uname);

        return true;
    }

    @Override
    public ArrayList<String> getFollower(String uname) {
        ListIterator<String> followers = template.opsForList().range("follower:" + uname, 0, -1).listIterator();
        ArrayList<String> result = new ArrayList<>();

        while (followers.hasNext()) {
            result.add(followers.next());
        }

        return result;
    }

    @Override
    public ArrayList<String> getFollowing(String uname) {
        ListIterator<String> following = template.opsForList().range("following:" + uname, 0, -1).listIterator();
        ArrayList<String> result = new ArrayList<>();

        while (following.hasNext()) {
            result.add(following.next());
        }

        return result;
    }

    @Override
    public boolean isFollowerOf(String uname) {
        ListIterator<String> follower = template.opsForList().range("follower:" + uname, 0, -1).listIterator();
        while (follower.hasNext()) {
            if(follower.next().equals(SimpleSecurity.getName()))
                return true;
        }
        return false;
    }

    @Override
    public Long sizeOfFollower(String uname) {
        return template.opsForList().size("follower:" + uname);
    }

    @Override
    public Long sizeOfFollowing(String uname) {
        return template.opsForList().size("following:" + uname);
    }
}


