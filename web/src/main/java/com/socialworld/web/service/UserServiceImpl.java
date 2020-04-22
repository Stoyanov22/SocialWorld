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
            if (dbUsers.stream().anyMatch(u -> u.getString("id").equals(id))) {
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
            if (dbUsers.stream().anyMatch(u -> u.getString("email").equals(email))) {
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
        ApiFuture<WriteResult> result = db.collection("Users").document(user.getId()).create(user);
        return result.isDone();
    }

    @Override
    public boolean editUser(User user) {
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("name", user.getName());
        updatedUser.put("countryId", user.getCountryId());
        updatedUser.put("dateOfBirth", user.getDateOfBirth());
        updatedUser.put("genderId", user.getGenderId());
        if(user.getPicture() != null && !user.getPicture().isEmpty()){
            updatedUser.put("picture", user.getPicture());
        }

        //Firebase wants this check
        if(user.getId() != null && !user.getId().isEmpty()){
            ApiFuture<WriteResult> result = db.collection("Users").document(user.getId()).update(updatedUser);
            return result.isDone();
        } else {
            return false;
        }
    }

    @Override
    public boolean removeUserById(String id) {
        ApiFuture<WriteResult> result = db.collection("Users").document(id).delete();
        return result.isDone();
    }
}
