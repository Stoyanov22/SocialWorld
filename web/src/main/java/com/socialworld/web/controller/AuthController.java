package com.socialworld.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class AuthController {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/login");
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
//    public ModelAndView login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password, @RequestParam Map<String, String> body ) {
    public ModelAndView login(@RequestParam String token) {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/login");
        return model;
    }
}
