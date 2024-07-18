package com.example.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUser(id);
    }

    @GetMapping("/email/{email}")
    public String getUserByEmail(@PathVariable String email) {
        return userService.loadUserByUsername(email).getUsername() + " lol";
    }
}
