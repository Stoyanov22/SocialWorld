package com.socialworld.web.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.socialworld.web.entity.Chat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    Firestore db = FirestoreClient.getFirestore();

    @Override
    public void addChat(Chat chat) {
            db.collection("Chats").document(chat.getId()).create(chat);
    }

    @Override
    public List<Chat> getChat(String fromUserId, String toUserId) {
        List<Chat> result = new ArrayList<>();
        try {
            List<QueryDocumentSnapshot> dbChats = db.collection("Chats").get().get().getDocuments();
            if (dbChats.stream().anyMatch(u -> u.getString("fromUserId").equals(fromUserId))) {
                List<QueryDocumentSnapshot> snapshotPosts = dbChats.stream().filter(u -> u.getString("fromUserId").equals(fromUserId) && u.getString("toUserId").equals(toUserId)).collect(Collectors.toList());
                for (QueryDocumentSnapshot post : snapshotPosts) {
                    result.add(post.toObject(Chat.class));
                }
            }
            if (dbChats.stream().anyMatch(u -> u.getString("fromUserId").equals(toUserId))) {
                List<QueryDocumentSnapshot> snapshotPosts = dbChats.stream().filter(u -> u.getString("fromUserId").equals(toUserId) && u.getString("toUserId").equals(fromUserId)).collect(Collectors.toList());
                for (QueryDocumentSnapshot post : snapshotPosts) {
                    result.add(post.toObject(Chat.class));
                }
            }
            result.sort((Comparator.comparing(Chat::getTimestamp)));
            return result;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }
}
