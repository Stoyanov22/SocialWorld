package com.socialworld.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class FeedController {

    @RequestMapping(value = {"/feed"}, method = RequestMethod.GET)
    public ModelAndView feed(HttpSession session) {
        ModelAndView model = new ModelAndView();
//        if (Objects.isNull(session.getAttribute("uid"))) {
//            model.setViewName("home/index");
//            return model;
//        }
        model.setViewName("feed/index");
        return model;
    }
}
