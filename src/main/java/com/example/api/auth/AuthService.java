package com.example.api.auth;

import com.example.api.security.JwtService;
import com.example.api.token.TokenService;
import com.example.api.user.User;
import com.example.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService{
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signUp(SignUpRequest request) {
        // build user
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // generate token and save it
        var token = jwtService.generateToken(user);
        var savedUser = userService.saveUser(user);
        tokenService.saveToken(savedUser, token);
        return new AuthResponse(token);

    }

    public AuthResponse signIn(SignInRequest request) {
        // create authentication token and authenticate
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authenticationToken);

        // check if user exists
        var user = userService.findUserByEmail(request.getEmail()).orElseThrow();

        // generate token and save it
        var token = jwtService.generateToken(user);
        var savedUser = userService.saveUser(user);
        tokenService.saveToken(savedUser, token);
        return new AuthResponse(token);
    }
}

