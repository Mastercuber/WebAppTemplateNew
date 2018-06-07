package org.avensio.common.persistence.dao.twitter;

import org.avensio.common.persistence.model.twitter.Post;

import java.util.ArrayList;

public interface IPostDao {
    // tweet operations
    Post addPost(String postText, String uname);
    ArrayList<String> getPostIds(String uname);
    Post getPost(String uuid);
    ArrayList<Post> getAllPosts();
    ArrayList<Post> getAllPosts(String uname);
    ArrayList<Post> getPaginatedPosts(String uname, int from, int to);
    boolean follow(String uname);
    boolean unfollow(String uname);
    ArrayList<String> getFollower(String uname);
    ArrayList<String> getFollowing(String uname);
    boolean isFollowerOf(String uname);
    Long sizeOfFollower(String uname);
    Long sizeOfFollowing(String uname);
}