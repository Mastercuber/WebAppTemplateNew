package org.avensio.common.web.controller.security;

import javax.validation.Valid;

import org.avensio.common.persistence.model.security.User;
import org.avensio.common.persistence.dao.jpa.security.IUserJpaDao;
import org.avensio.common.service.security.IUserService;
import org.avensio.common.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
class UserController {

    @Autowired
    private IUserJpaDao userJpaDao;

    @Autowired
    private IUserService userService;

    //

    @RequestMapping
    public ModelAndView list() {
        Iterable<User> users = this.userJpaDao.findAll();
        return new ModelAndView("tl/list", "users", users);
    }

    @RequestMapping("{id}")
    public ModelAndView view(@PathVariable("id") final User user) {
        return new ModelAndView("tl/view", "user", user);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid final User user, final BindingResult result, final RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("tl/form", "formErrors", result.getAllErrors());
        }
        try {
            userService.registerNewUser(user);
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("tl/form", "user", user);
        }
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/user/{user.id}", "user.id", user.getId());
    }

    @RequestMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") final Long id) {
        this.userJpaDao.delete(id);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView modifyForm(@PathVariable("id") final User user) {
        return new ModelAndView("tl/form", "user", user);
    }

    // the form

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute final User user) {
        return "tl/form";
    }

}
