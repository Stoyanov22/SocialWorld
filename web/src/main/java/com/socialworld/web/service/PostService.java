package com.socialworld.web.service;

import com.socialworld.web.entity.Post;
import com.socialworld.web.entity.User;

import java.util.List;

public interface PostService {

    List<Post> getPostsByUserId(String userId);

    List<Post> getPostsForUser(User user);

    void addPost(Post post);

    Post getPost(String id);

    void removePost(String id);
}
