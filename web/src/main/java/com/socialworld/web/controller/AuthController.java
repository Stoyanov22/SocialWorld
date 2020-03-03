package com.socialworld.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @RequestMapping(value= {"/login"}, method= RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/login");
        return model;
    }

    @RequestMapping(value= {"/login"}, method= RequestMethod.POST)
    public ModelAndView login(@RequestParam (value="email") String email, @RequestParam(value="password") String password) {

        ModelAndView model = new ModelAndView();
        model.setViewName("auth/login");
        return model;
    }
}
