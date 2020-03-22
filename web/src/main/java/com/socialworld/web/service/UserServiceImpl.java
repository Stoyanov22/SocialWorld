package com.socialworld.web.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.socialworld.web.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {

    private Firestore db = FirestoreClient.getFirestore();

    @Override
    public List<User> getAll() {
        try {
            List<QueryDocumentSnapshot> dbUsers = db.collection("Users").get().get().getDocuments();
            List<User> result = new ArrayList<>();
            for (QueryDocumentSnapshot user : dbUsers) {
                result.add(user.toObject(User.class));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public User getUserById(String id) {
        try {
            List<QueryDocumentSnapshot> dbUsers = db.collection("Users").get().get().getDocuments();
            if(dbUsers.stream().anyMatch(u -> u.getId().equals(id))){
                return dbUsers.stream().filter(u -> u.getId().equals(id)).findFirst().get().toObject(User.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            List<QueryDocumentSnapshot> dbUsers = db.collection("Users").get().get().getDocuments();
            if(dbUsers.stream().anyMatch(u -> u.getString("email").equals(email))){
                return dbUsers.stream().filter(u -> u.getString("email").equals(email)).findFirst().get().toObject(User.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public boolean addUser(User user) {
        ApiFuture<WriteResult> result = db.collection("Users").document().create(user);
        return result.isDone();
    }

    @Override
    public boolean editUser(User user) {
        Map<String, Object> editUser = new HashMap<>();
        editUser.put("uid", user.getUid());
        editUser.put("email", user.getEmail());
        editUser.put("name", user.getName());
        editUser.put("countryId", user.getCountryId());
        editUser.put("dateOfBirth", user.getDateOfBirth());
        editUser.put("genderId", user.getGenderId());

        ApiFuture<WriteResult> result = db.collection("Users").document(user.getId()).set(editUser);
        return result.isDone();
    }

    @Override
    public boolean removeUserById(String id) {
        ApiFuture<WriteResult> result = db.collection("Users").document(id).delete();
        return result.isDone();
    }
}
