package com.socialworld.web.service;

import com.socialworld.web.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getUserById(String id);

    User getUserByEmail(String email);

    void addUser(User user);

    void editUser(User user);

    void removeUserById(String id);
}
