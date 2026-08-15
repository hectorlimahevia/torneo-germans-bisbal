package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.Role;

public interface RoleService {

    Role save(Role role);

    void addRoleToUser(String username, String roleName);

    void removeRoleFromUser(String username, String roleName);
}
