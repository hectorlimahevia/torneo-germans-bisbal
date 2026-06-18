package com.ironhack.torneo_germans_bisbal_api.controller;


import com.ironhack.torneo_germans_bisbal_api.model.User;
import com.ironhack.torneo_germans_bisbal_api.service.RoleService;
import com.ironhack.torneo_germans_bisbal_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final RoleService roleService;

    /**
     * Get a list of all users
     *
     * @return list of all users
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Save a new user
     *
     * @param user the user to be saved
     */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody User user) {
        userService.saveUser(user);
        roleService.addRoleToUser(user.getUsername(), "ROLE_USER");
    }
}


