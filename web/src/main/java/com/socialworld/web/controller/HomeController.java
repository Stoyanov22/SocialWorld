package com.socialworld.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.socialworld.web.entity.User;
import com.socialworld.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView home(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.nonNull(session.getAttribute("uid"))) {
            model.setViewName("feed/index");
            return model;
        }
        model.setViewName("home/index");
        return model;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.POST)
    public ModelAndView home(HttpSession session, @RequestParam String uid, @RequestParam String email) {
        ModelAndView model = new ModelAndView();
        try {
            if (userService.getUserByEmail(email) == null) {
                UserRecord userRecord = firebaseAuth.getUser(uid);
                User user = new User(userRecord.getUid(), userRecord.getEmail(), userRecord.getDisplayName());
                userService.addUser(user);
            }

        } catch (FirebaseAuthException e) {
            model.setViewName("home/index");
            return model;
        }
        session.setMaxInactiveInterval(1209600);
        session.setAttribute("uid", uid);
        session.setAttribute("email", email);
        model.addObject("session", session);
        model.setViewName("feed/index");
        return model;
    }
}
