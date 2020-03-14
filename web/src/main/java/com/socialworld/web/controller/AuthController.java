package com.socialworld.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(HttpSession session) {
        if (session != null) {
            //TODO: Send user to NewsFeed page.
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/login");
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String uid, @RequestParam String email, HttpSession session) {
        ModelAndView model = new ModelAndView();
        session.setMaxInactiveInterval(1209600);
        session.setAttribute("uid", uid);
        session.setAttribute("email", email);
        model.addObject("session", session);
        model.setViewName("/");
        return model;
    }
}
