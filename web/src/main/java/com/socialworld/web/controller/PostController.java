package com.socialworld.web.controller;

import com.socialworld.web.entity.Comment;
import com.socialworld.web.service.CommentService;
import com.socialworld.web.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = {"/like"}, method = RequestMethod.POST)
    public void like(HttpSession session, @RequestParam String postId) {
        postService.like(postId, session.getAttribute("uid").toString());
    }

    @RequestMapping(value = {"/comment"}, method = RequestMethod.POST)
    public void comment(HttpSession session, @RequestParam String postId, @RequestParam String text) {
        String userId = session.getAttribute("uid").toString();
        Date date = new Date();
        Comment comment = new Comment(userId + date.getTime(), text, postId, userId, date);
        commentService.addComment(comment);
    }

}
