package com.example.api.security;

import com.example.api.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        // Check if the request has token
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        // Extract token and revoke it
        var jwtToken = authHeader.substring(7);
        var token = tokenService.findByToken(jwtToken);
        if (token != null) {
            token.setExpired(true);
            token.setRevoked(true);
            tokenService.saveToken(token);
            SecurityContextHolder.clearContext();
        }
    }
}

