package com.socialworld.web.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.socialworld.web.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private Firestore db = FirestoreClient.getFirestore();

    @Override
    public void addComment(Comment comment) {
        db.collection("Comments").document(comment.getId()).create(comment);
    }

    @Override
    public List<Comment> getCommentsByPost(String postId) {
        try {
            List<QueryDocumentSnapshot> dbComments = db.collection("Comments").get().get().getDocuments();
            if (dbComments.stream().anyMatch(u -> u.getString("postId").equals(postId))) {
                List<QueryDocumentSnapshot> snapshotPosts = dbComments.stream().filter(u -> u.getString("postId").equals(postId)).collect(Collectors.toList());
                List<Comment> result = new ArrayList<>();
                for (QueryDocumentSnapshot post : snapshotPosts) {
                    result.add(post.toObject(Comment.class));
                }
                return result;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }
}
