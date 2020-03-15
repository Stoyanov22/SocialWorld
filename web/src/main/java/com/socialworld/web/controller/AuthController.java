package com.socialworld.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;

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
    public ModelAndView login(HttpSession session, @RequestParam String uid, @RequestParam String email) {
        ModelAndView model = new ModelAndView();
        session.setMaxInactiveInterval(1209600);
        session.setAttribute("uid", uid);
        session.setAttribute("email", email);
        model.addObject("session", session);
        model.setViewName("/");
        return model;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public ModelAndView register(HttpSession session) {
        if (session != null) {
            //TODO: Send user to NewsFeed page.
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/register");
        return model;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public ModelAndView register(HttpSession session, @RequestParam String email, @RequestParam String password,
                                 @RequestParam String name, @RequestParam String gender, @RequestParam String country,
                                 @RequestParam String birthday, @RequestParam String picture) {
        if (session != null) {
            //TODO: Send user to NewsFeed page.
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/register");
        return model;
    }
}
