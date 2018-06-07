package org.avensio.common.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class PathController {

    //

    // @RequestMapping("/login")
    // public String list() {
    // return "loginPage";
    // }

    @RequestMapping("/")
    public String home() {
        return "redirect:/user";
    }

}
