package com.socialworld.web.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
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
            for (DocumentSnapshot user : dbUsers) {
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
            if (dbUsers.stream().anyMatch(u -> u.getId().equals(id))) {
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
    public void addUser(User user) {
        db.collection("Users").document(user.getId()).create(user);
    }

    @Override
    public void editUser(User user) {
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("name", user.getName());
        updatedUser.put("countryId", user.getCountryId());
        updatedUser.put("dateOfBirth", user.getDateOfBirth());
        updatedUser.put("genderId", user.getGenderId());
        if (user.getPicture() != null && !user.getPicture().isEmpty()) {
            updatedUser.put("picture", user.getPicture());
        }

        //Firebase requires this check
        if (user.getId() != null && !user.getId().isEmpty()) {
            db.collection("Users").document(user.getId()).update(updatedUser);
        }
    }

    @Override
    public void removeUserById(String id) {
        db.collection("Users").document(id).delete();
    }

    @Override
    public List<User> findUsersByName(String name) {
        List<User> result = new ArrayList<>();
        try {
            List<QueryDocumentSnapshot> queryDocumentSnapshots = db.collection("Users").get().get().getDocuments();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                if(document.getString("name").toLowerCase().contains(name.toLowerCase())){
                    result.add(document.toObject(User.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        return result;
    }

    @Override
    public void followUser(User user, User followedUser) {
        List<String> followedUsers = user.getFollowedUsers();
        followedUsers.add(followedUser.getId());
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("followedUsers", followedUsers);

        List<String> followers = followedUser.getFollowers();
        followers.add(user.getId());
        Map<String, Object> updatedFollowedUser = new HashMap<>();
        updatedFollowedUser.put("followers", followers);

        //Firebase requires this check
        if (user.getId() != null && !user.getId().isEmpty()) {
            db.collection("Users").document(user.getId()).update(updatedUser);
            db.collection("Users").document(followedUser.getId()).update(updatedFollowedUser);
        }
    }
}
