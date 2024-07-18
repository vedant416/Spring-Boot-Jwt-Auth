package com.example.api.auth;

import com.example.api.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignUpRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
