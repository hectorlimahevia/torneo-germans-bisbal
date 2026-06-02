package com.ironhack.torneo_germans_bisbal_api.demo;

import com.ironhack.torneo_germans_bisbal_api.model.Role;
import com.ironhack.torneo_germans_bisbal_api.model.User;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Club;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Field;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Team;
import com.ironhack.torneo_germans_bisbal_api.model.enums.Category;
import com.ironhack.torneo_germans_bisbal_api.repository.ClubRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.FieldRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.TeamRepository;
import com.ironhack.torneo_germans_bisbal_api.service.RoleService;
import com.ironhack.torneo_germans_bisbal_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    private final ClubRepository clubRepository;
    private final FieldRepository fieldRepository;
    private final TeamRepository teamRepository;

    @Override
    public void run(String... args) throws Exception {

        // ROLES
        roleService.save(new Role("ROLE_USER"));
        roleService.save(new Role("ROLE_ADMIN"));

        // USERS
        userService.saveUser(new User("John Doe", "john", "1234"));
        userService.saveUser(new User("James Smith", "james", "1234"));
        userService.saveUser(new User("Jane Carry", "jane", "1234"));
        userService.saveUser(new User("Chris Anderson", "chris", "1234"));

        roleService.addRoleToUser("john", "ROLE_USER");
        roleService.addRoleToUser("james", "ROLE_ADMIN");
        roleService.addRoleToUser("jane", "ROLE_USER");
        roleService.addRoleToUser("chris", "ROLE_ADMIN");
        roleService.addRoleToUser("chris", "ROLE_USER");

        // CLUBS
        Club ues = clubRepository.save(new Club(
                null,
                "UE Santboiana",
                "Sant Boi",
                "Spain",
                "https://www.uesantboiana.com/IMAGES/LOGOS/uesantboiana-escut-blau.png"
        ));

        Club barca = clubRepository.save(new Club(
                null,
                "Barça Rugby",
                "Barcelona",
                "Spain",
                null
        ));

        Club geieg = clubRepository.save(new Club(
                null,
                "GEiEG",
                "Girona",
                "Spain",
                null
        ));

        // FIELDS
        fieldRepository.save(new Field(null, "Campo 1", "UES Rugby Complex"));
        fieldRepository.save(new Field(null, "Campo 2", "UES Rugby Complex"));
        fieldRepository.save(new Field(null, "Campo 3", "UES Rugby Complex"));
        fieldRepository.save(new Field(null, "Campo 4", "UES Rugby Complex"));

        // TEAMS
        teamRepository.save(new Team(null, "UES SUB6", Category.SUB6, ues));
        teamRepository.save(new Team(null, "UES SUB8", Category.SUB8, ues));
        teamRepository.save(new Team(null, "UES SUB10", Category.SUB10, ues));
        teamRepository.save(new Team(null, "UES SUB12", Category.SUB12, ues));

        teamRepository.save(new Team(null, "Barça SUB10", Category.SUB10, barca));
        teamRepository.save(new Team(null, "Barça SUB12", Category.SUB12, barca));

        teamRepository.save(new Team(null, "GEiEG SUB8", Category.SUB8, geieg));
        teamRepository.save(new Team(null, "GEiEG SUB10", Category.SUB10, geieg));
    }
}