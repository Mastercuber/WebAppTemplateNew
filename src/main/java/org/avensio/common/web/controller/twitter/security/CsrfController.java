package org.avensio.common.web.controller.twitter.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @RequestMapping("/auth/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
