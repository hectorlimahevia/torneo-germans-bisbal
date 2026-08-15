package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUser(String username);

    List<User> getUsers();
}

