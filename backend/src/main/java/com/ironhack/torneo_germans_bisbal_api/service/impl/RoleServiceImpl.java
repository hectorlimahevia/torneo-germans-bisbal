package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.exception.LastAdminRemovalException;
import com.ironhack.torneo_germans_bisbal_api.model.Role;
import com.ironhack.torneo_germans_bisbal_api.model.User;
import com.ironhack.torneo_germans_bisbal_api.repository.RoleRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.UserRepository;
import com.ironhack.torneo_germans_bisbal_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {


    private final UserRepository userRepository;


    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);

        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        user.getRoles().add(role);

        userRepository.save(user);
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) {
        log.info("Removing role {} from user {}", roleName, username);

        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        if ("ROLE_ADMIN".equals(roleName)) {

            long adminCount = userRepository.countByRolesName("ROLE_ADMIN");

            if (adminCount <= 1) {
                throw new LastAdminRemovalException(
                        "The last administrator cannot be removed."
                );
            }
        }

        user.getRoles().remove(role);

        userRepository.save(user);
    }

}
