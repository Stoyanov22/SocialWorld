package com.socialworld.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.socialworld.web.entity.CountryConstants;
import com.socialworld.web.entity.GenderConstants;
import com.socialworld.web.entity.Post;
import com.socialworld.web.entity.User;
import com.socialworld.web.service.CommentService;
import com.socialworld.web.service.PostService;
import com.socialworld.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

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
            List<Post> posts = postService.getPostsByUserId(user.getId());
            model.addObject("user", user);
            model.addObject("gender", GenderConstants.getGenderName(user.getGenderId()));
            model.addObject("country", CountryConstants.getCountryName(user.getCountryId()));
            model.addObject("posts", posts);
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
        if (dateOfBirth != null && dateOfBirth != "") {
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

    @RequestMapping(value = {"/remove_post"}, method = RequestMethod.POST)
    public void removePost(HttpSession session, @RequestParam String postId) {
        postService.removePost(postId);
    }

    @RequestMapping(value = {"/profile/{userId}"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession session, @PathVariable String userId) {
        ModelAndView model = new ModelAndView();
        if (Objects.isNull(session.getAttribute("uid"))) {
            model.setViewName("home/index");
            return model;
        }
        if (userId.equals(session.getAttribute("uid"))) {
            model.setViewName("redirect:/my_profile");
            return model;
        } else {
            User user = userService.getUserById(userId);
            if (user != null) {
                List<Post> posts = postService.getPostsByUserId(user.getId());
                model.addObject("user", user);
                model.addObject("gender", GenderConstants.getGenderName(user.getGenderId()));
                model.addObject("country", CountryConstants.getCountryName(user.getCountryId()));
                model.addObject("posts", posts);
                model.addObject("userService", userService);
                model.addObject("commentService", commentService);
                if (user.getFollowers() != null && user.getFollowers().contains(session.getAttribute("uid").toString())) {
                    model.addObject("isFollowed", true);
                } else {
                    model.addObject("isFollowed", false);
                }
                model.setViewName("user/user_profile");
                return model;
            } else {
                model.setViewName("user/explore");
                return model;
            }
        }
    }

    @RequestMapping(value = {"/follow_user"}, method = RequestMethod.POST)
    public ModelAndView followUser(HttpSession session, @RequestParam String followedId) {
        ModelAndView model = new ModelAndView();
        User user = userService.getUserById(session.getAttribute("uid").toString());
        User followedUser = userService.getUserById(followedId);
        userService.followUser(user, followedUser);
        return userProfile(session, followedId);
    }

    @RequestMapping(value = {"/unfollow_user"}, method = RequestMethod.POST)
    public ModelAndView unfolloweUser(HttpSession session, @RequestParam String unfollowedId) {
        ModelAndView model = new ModelAndView();
        User user = userService.getUserById(session.getAttribute("uid").toString());
        User unfollowedUser = userService.getUserById(unfollowedId);
        userService.unfollowUser(user, unfollowedUser);
        return userProfile(session, unfollowedId);
    }
}