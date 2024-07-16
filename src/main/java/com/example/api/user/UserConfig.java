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
        return args -> userRepository.saveAll(users);
    }
}
