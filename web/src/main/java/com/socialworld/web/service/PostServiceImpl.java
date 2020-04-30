package com.socialworld.web.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.socialworld.web.entity.Post;
import com.socialworld.web.entity.User;

import java.util.List;

public class PostServiceImpl implements PostService {

    private Firestore db = FirestoreClient.getFirestore();

    @Override
    public List<Post> getPostsByUserId(String userId) {
        return null;
    }

    @Override
    public List<Post> getPostsForUser(User user) {
        return null;
    }

    @Override
    public void addPost() {

    }

    @Override
    public void getPost(String id) {

    }

    @Override
    public void editPost(String id) {

    }

    @Override
    public void removePost(String id) {

    }
}
