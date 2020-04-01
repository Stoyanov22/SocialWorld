package com.socialworld.web.controller;

import com.socialworld.web.entity.User;
import com.socialworld.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class FeedController {

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/feed"}, method = RequestMethod.GET)
    public ModelAndView feed(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.isNull(session.getAttribute("uid"))) {
            model.setViewName("home/index");
            return model;
        }
        User user = userService.getUserByEmail(session.getAttribute("email").toString());
        model.addObject("user", user);
        model.setViewName("feed/index");
        return model;
    }
}
