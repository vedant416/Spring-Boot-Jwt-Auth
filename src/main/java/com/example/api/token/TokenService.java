package com.example.api.token;

import com.example.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    public boolean isTokenValid(String jwtToken) {
        var token = tokenRepository.findByToken(jwtToken);
        if (token == null) {
            return false;
        }
        return !token.isExpired() && !token.isRevoked();
    }

    public void saveToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Token findByToken(String jwtToken) {
        return tokenRepository.findByToken(jwtToken);
    }

}
