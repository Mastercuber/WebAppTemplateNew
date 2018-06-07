package org.avensio.common.spring.interceptor;

import org.avensio.common.persistence.dao.twitter.security.IAuthenticationDao;
import org.avensio.common.security.SimpleSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleSecurityCookieInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private IAuthenticationDao authenticationDao;

    // Is executed before invoking the handler
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();

        if (!ObjectUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth")) {
                    String auth = cookie.getValue();

                    if (auth != null) {
                        String username = authenticationDao.getUsernameByAuth(auth);

                        if (username != null && !username.isEmpty()) {
                            SimpleSecurity.setLocalThread(username);
                        }
                    }
                }
            }
        }

        return true;
    }

    // Is executed after rendering the view
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SimpleSecurity.unsetUser();
    }
}
