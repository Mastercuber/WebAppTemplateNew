package org.avensio.common.web.controller.twitter.security;

import org.avensio.common.persistence.dao.twitter.security.IAuthenticationDao;
import org.avensio.common.persistence.dao.twitter.security.IUserDao;
import org.avensio.common.persistence.model.security.twitter.TwitterUser;
import org.avensio.common.security.SimpleSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {
    @Autowired
    private IAuthenticationDao authenticationDao;
    private final Duration TIMEOUT = Duration.ofMinutes(15);
    @Autowired
    private IUserDao mainRepo;

    @RequestMapping(value={"/auth/login"}, method=RequestMethod.GET)
    public ModelAndView getLoginView() {
        return new ModelAndView("twitter/login");
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("user") TwitterUser user, HttpServletResponse response, Model model) {
        // Is the user currently authenticated?
        if (authenticationDao.auth(user.getUsername(), user.getPassword())) {
            String auth = authenticationDao.addAuth(user.getUsername(), TIMEOUT.getSeconds(), TimeUnit.SECONDS);
            Cookie cookie = new Cookie("auth", auth);
            cookie.setMaxAge((int) TIMEOUT.getSeconds());
            response.addCookie(cookie);

            model.addAttribute("from", 0);
            model.addAttribute("to", 20);

            SimpleSecurity.setLocalThread(user.getUsername());
            return "redirect:/auth/timeline";
        }

        return "twitter/login";
    }

    @RequestMapping(value="/auth/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") TwitterUser user, HttpServletResponse response, Model model) {
        mainRepo.addUser(user);
        String auth = authenticationDao.addAuth(user.getUsername(), TIMEOUT.getSeconds(), TimeUnit.SECONDS);
        Cookie cookie = new Cookie("auth", auth);
        cookie.setMaxAge((int) TIMEOUT.getSeconds());
        response.addCookie(cookie);

        model.addAttribute("from", 0);
        model.addAttribute("to", 20);

        SimpleSecurity.setLocalThread(user.getUsername());
        return "redirect:/auth/timeline";
    }

    @RequestMapping(value = {"/auth/logout"}, method = RequestMethod.GET)
    public String logout() {
        if (SimpleSecurity.isSignedIn()) {
            authenticationDao.deleteAuth(SimpleSecurity.getName());
        }
        return "redirect:/auth/login";
    }
}