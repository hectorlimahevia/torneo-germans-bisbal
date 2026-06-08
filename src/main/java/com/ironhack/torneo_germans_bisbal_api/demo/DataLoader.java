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
import com.ironhack.torneo_germans_bisbal_api.model.entity.Match;
import com.ironhack.torneo_germans_bisbal_api.model.enums.MatchStatus;
import com.ironhack.torneo_germans_bisbal_api.repository.MatchRepository;
import com.ironhack.torneo_germans_bisbal_api.repository.RuleRepository;
import com.ironhack.torneo_germans_bisbal_api.model.entity.ScoringRule;
import com.ironhack.torneo_germans_bisbal_api.model.entity.ScheduleRule;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    private final ClubRepository clubRepository;
    private final FieldRepository fieldRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    private final RuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {
       //protegiendo el dataloader de duplicados
        if (clubRepository.count() > 0) {
            return;
        }
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
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsjavn6dgnDe75jfE0gdR7hc48s4nnqk7CNg&s"
        ));

        Club geieg = clubRepository.save(new Club(
                null,
                "GEiEG",
                "Girona",
                "Spain",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSLjGMXpfNLlZoP4uwY2KV8A-GAO7c12sBpRA&s"
        ));

        // FIELDS
        Field field1 = fieldRepository.save(
                new Field(null, "Campo 1", "UES Rugby Complex"));

        Field field2 = fieldRepository.save(
                new Field(null, "Campo 2", "UES Rugby Complex"));

        Field field3 = fieldRepository.save(
                new Field(null, "Campo 3", "UES Rugby Complex"));

        Field field4 = fieldRepository.save(
                new Field(null, "Campo 4", "UES Rugby Complex"));


// TEAMS
        Team uesSub6 = teamRepository.save(
                new Team(null, "UES SUB6", Category.SUB6, ues));

        Team uesSub8 = teamRepository.save(
                new Team(null, "UES SUB8", Category.SUB8, ues));

        Team uesSub10 = teamRepository.save(
                new Team(null, "UES SUB10", Category.SUB10, ues));

        Team uesSub12 = teamRepository.save(
                new Team(null, "UES SUB12", Category.SUB12, ues));

        Team barcaSub10 = teamRepository.save(
                new Team(null, "Barça SUB10", Category.SUB10, barca));

        Team barcaSub12 = teamRepository.save(
                new Team(null, "Barça SUB12", Category.SUB12, barca));

        Team geiegSub8 = teamRepository.save(
                new Team(null, "GEiEG SUB8", Category.SUB8, geieg));

        Team geiegSub10 = teamRepository.save(
                new Team(null, "GEiEG SUB10", Category.SUB10, geieg));

        //Matches
        matchRepository.save(
                Match.builder()
                        .localTeam(uesSub10)
                        .visitorTeam(barcaSub10)
                        .field(field1)
                        .matchDate(LocalDate.now())
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(10, 15))
                        .localTries(4)
                        .visitorTries(2)
                        .status(MatchStatus.FINISHED)
                        .roundNumber(1)
                        .build()
        );

        matchRepository.save(
                Match.builder()
                        .localTeam(geiegSub10)
                        .visitorTeam(uesSub10)
                        .field(field2)
                        .matchDate(LocalDate.now())
                        .startTime(LocalTime.of(10, 20))
                        .endTime(LocalTime.of(10, 35))
                        .localTries(1)
                        .visitorTries(1)
                        .status(MatchStatus.FINISHED)
                        .roundNumber(1)
                        .build()
        );

        matchRepository.save(
                Match.builder()
                        .localTeam(barcaSub10)
                        .visitorTeam(geiegSub10)
                        .field(field3)
                        .matchDate(LocalDate.now())
                        .startTime(LocalTime.of(10, 40))
                        .endTime(LocalTime.of(10, 55))
                        .localTries(5)
                        .visitorTries(4)
                        .status(MatchStatus.FINISHED)
                        .roundNumber(1)
                        .build()
        );

        //Rules
        // RULES

        ruleRepository.save(
                new ScoringRule(
                        null,
                        "Victory",
                        "A victory grants 3 points",
                        3
                )
        );

        ruleRepository.save(
                new ScoringRule(
                        null,
                        "Draw",
                        "A draw grants 1 point",
                        1
                )
        );

        ruleRepository.save(
                new ScoringRule(
                        null,
                        "Offensive Bonus",
                        "More than 3 tries grants 1 bonus point",
                        1
                )
        );

        ruleRepository.save(
                new ScheduleRule(
                        null,
                        "SUB10 Schedule",
                        "SUB10 matches are played between 10:30 and 12:30",
                        Category.SUB10
                )
        );

        ruleRepository.save(
                new ScheduleRule(
                        null,
                        "SUB12 Schedule",
                        "SUB12 matches are played between 14:00 and 16:00",
                        Category.SUB12
                )
        );
    }
}