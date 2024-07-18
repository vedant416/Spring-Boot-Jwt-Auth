package com.example.api;

import com.example.api.auth.AuthService;
import com.example.api.auth.SignUpRequest;
import com.example.api.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @RequestMapping("/api/hello")


    @Bean
    CommandLineRunner commandLineRunner(AuthService authService) {
        return args -> {
            var user = SignUpRequest.builder()
                    .firstname("user_firstname")
                    .lastname("user_lastname")
                    .email("user_email")
                    .password("user_password")
                    .role(Role.USER)
                    .build();

            var admin = SignUpRequest.builder()
                    .firstname("admin_firstname")
                    .lastname("admin_lastname")
                    .email("admin_email")
                    .password("admin_password")
                    .role(Role.ADMIN)
                    .build();

            var userToken = authService.signUp(user);
            var adminToken = authService.signUp(admin);
            System.out.println("User token: " + userToken.getToken());
            System.out.println("Admin token: " + adminToken.getToken());
        };
    }
}

@RestController
@RequestMapping("/api/hello")
class HelloController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "Hello, World!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String helloUser() {
        return "Hello, World!";
    }
}
