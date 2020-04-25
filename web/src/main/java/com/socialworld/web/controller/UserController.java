package com.socialworld.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.socialworld.web.entity.CountryConstants;
import com.socialworld.web.entity.GenderConstants;
import com.socialworld.web.entity.User;
import com.socialworld.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @RequestMapping(value = {"/my_profile"}, method = RequestMethod.GET)
    public ModelAndView myProfile(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.isNull(session.getAttribute("uid"))) {
            model.setViewName("home/index");
            return model;
        }
        User user = userService.getUserById(session.getAttribute("uid").toString());
        if (user != null) {
            model.addObject("user", user);
            model.addObject("gender", GenderConstants.getGenderName(user.getGenderId()));
            model.addObject("country", CountryConstants.getCountryName(user.getCountryId()));
            model.setViewName("user/my_profile");
        } else {
            model.setViewName("home/index");
        }
        return model;
    }

    @RequestMapping(value = {"/edit_profile"}, method = RequestMethod.GET)
    public ModelAndView editProfile(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.isNull(session.getAttribute("uid"))) {
            model.setViewName("home/index");
            return model;
        }
        User user = userService.getUserById(session.getAttribute("uid").toString());
        if (user != null) {
            model.addObject("user", user);
            model.addObject("genders", GenderConstants.getGendersMap());
            model.addObject("countries", CountryConstants.getCountriesMap());
            model.setViewName("user/edit_profile");
        } else {
            model.setViewName("home/index");
        }
        return model;
    }

    @RequestMapping(value = {"/edit_profile"}, method = RequestMethod.POST)
    public ModelAndView editProfile(HttpSession session, @RequestParam Object name, @RequestParam String dateOfBirth,
                                    @RequestParam String genderId, @RequestParam String countryId, @RequestParam String pictureUrl) {
        ModelAndView model = new ModelAndView();
        Date dob = null;
        if (dateOfBirth != null && dateOfBirth != ""){
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                dob = formatter.parse(dateOfBirth);
            } catch (ParseException e) {
                System.err.println("Couldn't parse date : " + dateOfBirth);
            }
        }

        User user = new User(session.getAttribute("uid").toString(), session.getAttribute("email").toString(), name.toString(), dob, Integer.parseInt(genderId), Integer.parseInt(countryId), pictureUrl);
        userService.editUser(user);
        model.setViewName("redirect:/my_profile");
        return model;
    }

    @RequestMapping(value = {"/remove_user"}, method = RequestMethod.POST)
    public ModelAndView removeUser(HttpSession session, @RequestParam String uid) {
        try {
            firebaseAuth.deleteUser(uid);
            userService.removeUserById(uid);
            session.invalidate();
        } catch (FirebaseAuthException e) {
            System.err.println("Couldn't remove user : " + uid);
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/index");
        return model;
    }
}