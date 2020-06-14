package com.socialworld.mobile.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Atanas Katsarov
 */
public class UserCollection {
    private Map<String, UserEntity> userMap;

    /**
     * Initialization constructor
     */
    public UserCollection(List<UserEntity> userList) {
        Map<String, UserEntity> userMap = new HashMap<>();
        for (UserEntity user : userList) {
            userMap.put(user.getId(), user);
        }

        this.userMap = userMap;
    }

    public UserCollection(Map<String, UserEntity> userMap) {
        this.userMap = new HashMap<>(userMap);
    }

    public Map<String, UserEntity> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, UserEntity> userMap) {
        this.userMap = userMap;
    }

    /**
     * @return Set of all users values
     */
    public Set<UserEntity> getUserSet() {
        return new HashSet<>(userMap.values());
    }

    /**
     * @return Set of all user ids
     */
    public Set<String> getUserIdsSet() {
        return userMap.keySet();
    }

    /**
     * @return List of all user ids
     */
    public List<String> getUserIdsList() {
        return new ArrayList<>(userMap.keySet());
    }

    /**
     * Adds a new user in the map of users
     *
     * @param user The user to add
     */
    public void addUser(UserEntity user) {
        userMap.put(user.getId(), user);
    }

    /**
     * Removes a user from the map of users
     *
     * @param user The user to remove
     */
    public void removeUser(UserEntity user) {
        userMap.remove(user.getId());
    }

    /**
     * Removes a user from the map of users by given user id
     *
     * @param userId The id of user to remove
     */
    public void removeUserById(String userId) {
        userMap.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCollection that = (UserCollection) o;
        return userMap.equals(that.userMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userMap);
    }

    @Override
    public String toString() {
        return "UserCollection{" +
                "userMap=" + userMap +
                '}';
    }
}
