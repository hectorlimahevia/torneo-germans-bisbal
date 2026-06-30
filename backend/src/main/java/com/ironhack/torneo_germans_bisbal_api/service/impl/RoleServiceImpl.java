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

    /**
     * Saves a new role to the database
     *
     * @param role the role to be saved
     * @return the saved role
     */
    @Override
    public Role save(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    /**
     * Adds a role to the user with the given username
     *
     * @param username the username of the user to add the role to
     * @param roleName the name of the role to be added
     */
    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);

        // Retrieve the user and role objects from the repository
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        // Add the role to the user's role collection
        user.getRoles().add(role);

        // Save the user to persist the changes
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
