package com.socialworld.web.service;

import com.socialworld.web.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getUserById(String id);

    User getUserByEmail(String email);

    boolean addUser(User user);

    boolean editUser(User user);

    boolean removeUserById(String id);
}
