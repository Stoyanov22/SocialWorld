package com.socialworld.web.controller;

import com.socialworld.web.entity.User;
import com.socialworld.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
public class ExploreController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/explore"}, method = RequestMethod.GET)
    public ModelAndView explore(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.nonNull(session.getAttribute("uid"))) {
            model.addObject("users", null);
            model.setViewName("explore/explore");
            return model;
        }
        model.setViewName("home/index");
        return model;
    }

    @RequestMapping(value = {"/explore"}, method = RequestMethod.POST)
    public ModelAndView explore(HttpSession session, @RequestParam String searchInput) {
        ModelAndView model = new ModelAndView();
        List<User> users = userService.findUsersByName(searchInput);
        model.addObject("users", users);
        model.setViewName("explore/explore");
        return model;
    }

    @RequestMapping(value = {"/followed"}, method = RequestMethod.GET)
    public ModelAndView followed(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.nonNull(session.getAttribute("uid"))) {
            User user = userService.getUserById(session.getAttribute("uid").toString());
            List<User> users = userService.getFollowedUsers(user);
            model.addObject("users", users);
            model.setViewName("explore/followed");
            return model;
        }
        model.setViewName("home/index");
        return model;
    }
}
