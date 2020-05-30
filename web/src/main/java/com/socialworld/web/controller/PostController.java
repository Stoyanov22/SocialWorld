package com.socialworld.web.controller;

import com.socialworld.web.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @RequestMapping(value = {"/like"}, method = RequestMethod.POST)
    public void like(HttpSession session, @RequestParam String postId) {
        postService.like(postId, session.getAttribute("uid").toString());
    }

}
