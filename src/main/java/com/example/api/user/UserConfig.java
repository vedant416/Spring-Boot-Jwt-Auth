package com.example.api.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunnerUser(UserRepository userRepository) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User(
                    i,
                    "firstName_" + i,
                    "lastName_" + i,
                    "mail_" + i,
                    "password_" + i
            );
            users.add(user);
        }
        return args -> {
            userRepository.saveAll(users);
            // find a user by mail
            User user = userRepository.findUserByEmail("mail_1");
            System.out.println("User found by username: " + user + "\n");

            // find a user by mail and password
            User user2 = userRepository.findUserByEmailAndPassword("mail_1", "password_1");
            System.out.println("User found by username and password: " + user2 + "\n");

            // find all users
            List<User> allUsers = userRepository.findAll();
            System.out.println("All users: " + allUsers);
        };
    }
}
