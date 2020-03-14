package com.socialworld.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView home(HttpSession session) {
        if (session != null) {
            //TODO: Send user to NewsFeed page.
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("home/index");
        return model;
    }
}
