package com.socialworld.web.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.socialworld.web.entity.Post;
import com.socialworld.web.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PostServiceImpl implements PostService {

    private Firestore db = FirestoreClient.getFirestore();

    @Override
    public List<Post> getPostsByUserId(String userId) {
        try {
            List<QueryDocumentSnapshot> dbPosts = db.collection("Posts").get().get().getDocuments();
            if (dbPosts.stream().anyMatch(u -> u.getString("userId").equals(userId))) {
                List<QueryDocumentSnapshot> snapshotPosts = dbPosts.stream().filter(u -> u.getString("userId").equals(userId)).collect(Collectors.toList());
                List<Post> result = new ArrayList<>();
                for (QueryDocumentSnapshot post : snapshotPosts) {
                    result.add(post.toObject(Post.class));
                }
                return result;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public List<Post> getPostsForUser(User user) {
        return null;
    }

    @Override
    public void addPost(Post post) {
        db.collection("Posts").document(post.getId()).create(post);
    }

    @Override
    public Post getPost(String id) {
        try {
            List<QueryDocumentSnapshot> dbPosts = db.collection("Posts").get().get().getDocuments();
            if (dbPosts.stream().anyMatch(p -> p.getId().equals(id))) {
                return dbPosts.stream().filter(p -> p.getId().equals(id)).findFirst().get().toObject(Post.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public void removePost(String id) {
        db.collection("Posts").document(id).delete();
    }
}
