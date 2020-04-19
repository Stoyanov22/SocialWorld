package com.socialworld.web.controller;

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

    @RequestMapping(value = {"/my_profile"}, method = RequestMethod.GET)
    public ModelAndView myProfile(HttpSession session) {
        ModelAndView model = new ModelAndView();
        if (Objects.isNull(session.getAttribute("uid"))) {
            model.setViewName("home/index");
            return model;
        }
        //TODO: change to getUserById when we update the documentID to be the UID
        User user = userService.getUserByEmail(session.getAttribute("email").toString());
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
        //TODO: change to getUserById when we update the documentID to be the UID
        User user = userService.getUserByEmail(session.getAttribute("email").toString());
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
    public ModelAndView editProfile(HttpSession session, @RequestParam String name, @RequestParam String dob,
                                    @RequestParam String genderId, @RequestParam String countryId, @RequestParam String picture) {
        ModelAndView model = new ModelAndView();
        Date dateOfBirth = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            dateOfBirth = formatter.parse(dob);
        } catch (ParseException e) {
            System.err.println("Couldn't parse date : " + dob);
        }
        User user = new User(session.getAttribute("uid").toString(), session.getAttribute("email").toString(), name, dateOfBirth, Integer.parseInt(genderId), Integer.parseInt(countryId), picture);
        boolean isDone = userService.editUser(user);
        if (!isDone) {
            System.err.println("Couldn't edit user : " + user);
        }
        model.setViewName("user/my_profile");
        return model;
    }
}