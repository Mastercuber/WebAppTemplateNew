package org.avensio.common.web.controller.twitter.security;

import java.util.ArrayList;

import org.avensio.common.persistence.dao.twitter.IPostDao;
import org.avensio.common.persistence.dao.twitter.security.IUserDao;
import org.avensio.common.persistence.model.twitter.Post;
import org.avensio.common.security.SimpleSecurity;
import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TwitterController {
    @Autowired
    private IPostDao postDao;
    @Autowired
    private IUserDao userDao;

    @RequestMapping(value="/auth/timeline", method=RequestMethod.GET)
    public String getTimelines(@Param("from") String from,
                               @Param("to") String to, Model model) {
        if(SimpleSecurity.isSignedIn()) {
            ArrayList<Post> allPosts = postDao.getAllPosts();
            model.addAttribute("posts", postDao.getPaginatedPosts(null, Integer.valueOf(from), Integer.valueOf(to)));

            model.addAttribute("postsListSize", allPosts.size());
            model.addAttribute("username", SimpleSecurity.getName());
            model.addAttribute("from", Integer.valueOf(from));
            model.addAttribute("to", Integer.valueOf(to));

            return "twitter/timeline";
        }
        return "redirect:/auth/login";
    }

    @RequestMapping(value="/auth/dashboard", method=RequestMethod.GET)
    public String getDashboard(Model model) {
        if(SimpleSecurity.isSignedIn()) {
            ArrayList<Post> allPosts = postDao.getAllPosts();
            model.addAttribute("posts", allPosts);

            model.addAttribute("postsListSize", allPosts.size());
            model.addAttribute("username", SimpleSecurity.getName());

            return "twitter/dashboard";
        }
        return "redirect:/auth/login";
    }

    @RequestMapping(value="/auth/privateTimeline", method=RequestMethod.GET)
    public String getPrivateTimeline(@ModelAttribute("user") String username, @ModelAttribute("from") int from,
                                     @ModelAttribute("to") int to, Model model) {
        if(SimpleSecurity.isSignedIn()) {
            ArrayList<Post> allPosts = postDao.getAllPosts(username);
            model.addAttribute("posts", postDao.getPaginatedPosts(username, from, to));
            model.addAttribute("postsListSize", allPosts.size());
            model.addAttribute("username", username);
            model.addAttribute("from", from);
            model.addAttribute("to", to);

            return "twitter/privateTimeline";
        }
        return "redirect:/auth/login";
    }

    @RequestMapping(value="/auth/search/{query}", method=RequestMethod.GET)
    public @ResponseBody ArrayList<String> getListOfUsernames(@PathVariable("query") String query) {
        return userDao.searchUsers(query);
    }

    @RequestMapping(value="/auth/post", method=RequestMethod.POST)
    public @ResponseBody Post savePost(@Param("postText") String postText) {
        return postDao.addPost(postText, SimpleSecurity.getName());
    }

    @RequestMapping(value="/auth/user/follower/{user}", method=RequestMethod.GET)
    public @ResponseBody ArrayList<String> getFollower(@PathVariable("user") String username) {
        return postDao.getFollower(username);
    }

    @RequestMapping(value="/auth/user/following/{user}", method=RequestMethod.GET)
    public @ResponseBody ArrayList<String> getFollowing(@PathVariable("user") String username) {
        return postDao.getFollowing(username);
    }

    @RequestMapping(value="/auth/user/follow/{user}", method=RequestMethod.PUT)
    public @ResponseBody boolean follow(@PathVariable("user") String username) {
        return postDao.follow(username);
    }

    @RequestMapping(value="/auth/user/follow/{user}", method=RequestMethod.DELETE)
    public @ResponseBody boolean unfollow(@PathVariable("user") String username) {
        return postDao.unfollow(username);
    }

    @RequestMapping(value="/auth/user/follower/size/{user}", method=RequestMethod.GET)
    public @ResponseBody Long sizeOfFollower(@PathVariable("user") String username) {
        return postDao.sizeOfFollower(username);
    }

    @RequestMapping(value="/auth/user/following/size/{user}", method=RequestMethod.GET)
    public @ResponseBody Long sizeOfFollowing(@PathVariable("user") String username) {
        return postDao.sizeOfFollowing(username);
    }

    @RequestMapping(value = "/auth/follower/{username}", method = RequestMethod.GET)
    public @ResponseBody String isFollowerOf(@PathVariable("username") String username) {
        return String.valueOf(postDao.isFollowerOf(username)) + ":" + SimpleSecurity.getName();
    }
}
