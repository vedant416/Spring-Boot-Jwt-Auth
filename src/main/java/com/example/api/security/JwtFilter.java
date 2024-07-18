package com.example.api.security;

import com.example.api.token.TokenService;
import com.example.api.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Check if the request has token
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // At this point the request has token
        // Check if the token has email or is already authenticated
        var token = authHeader.substring(7);
        var email = jwtService.extractEmailFromToken(token);
        var securityContext = SecurityContextHolder.getContext();
        if (email == null || securityContext.getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // At this point email is not null and user is not authenticated
        // Check if user exists and get user details from db
        // Check if the token is valid and not expired
        var userDetails = this.userService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(token, userDetails.getUsername()) || !tokenService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // At this point email is in db, the token is valid and token is not expired
        // Finally authenticate the user
        // Create authentication token and set it in security context
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(authToken);

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}