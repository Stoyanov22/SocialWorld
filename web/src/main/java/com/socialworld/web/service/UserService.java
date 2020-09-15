package com.socialworld.web.service;

import com.socialworld.web.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> getAll();

    User getUserById(String id);

    User getUserByEmail(String email);

    void addUser(User user);

    void editUser(User user);

    void removeUserById(String id);

    List<User> findUsersByName(String name);

    void followUser(User user, User followedUser);

    void unfollowUser(User user, User unfollowedUser);

    List<User> getFollowedUsers(User user);

    List<User> getFollowers(User user);

    Set<User> getAmountOfRandomUsers(int amount);
}
