package com.socialworld.web.service;

import com.socialworld.web.entity.Comment;

import java.util.List;


public interface CommentService {

    void addComment(Comment comment);

    List<Comment> getCommentsByPost(String postId);
}
