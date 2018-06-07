package org.avensio.common.message;

import org.avensio.common.persistence.dao.twitter.IPostDao;
import org.avensio.common.persistence.model.twitter.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Multicast the info about a new post from a user to the follower of the user
 */
@Component
public class MultiCastPostInfoToFollower {
    @Autowired
    private IPostDao postDao;
    @Autowired
    private SimpMessagingTemplate msgtemplate;

    // Got message from subscribed Channel -> send via redisTemplate
    public void receiveMessage(String postId) {
        sendInfoToFollower(postDao.getPost(postId));
    }

    public void sendInfoToFollower(Post post) {
        ArrayList<String> users = postDao.getFollower(post.getUsername());

        for(String username : users) {
            String dateText = post.getDateText();
            String date = dateText.split(" ")[0];
            String time = dateText.split(" ")[1];
            msgtemplate.convertAndSend("/user/" + username + "/message", post.getUsername() +
                    " hat etwas gepostet am " + date + " um " + time);
        }
    }
}
