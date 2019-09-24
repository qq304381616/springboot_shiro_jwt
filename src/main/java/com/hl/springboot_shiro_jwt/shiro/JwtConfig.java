package com.hl.springboot_shiro_jwt.shiro;

import com.hl.springboot_shiro_jwt.domain.Token;
import com.hl.springboot_shiro_jwt.domain.User;
import com.hl.springboot_shiro_jwt.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
public class JwtConfig {

    @Autowired
    private TokenRepository tokenRepository;

    public String createToken(User user) {
        Token token;
        Optional<Token> optional = tokenRepository.findById(user.getUsername());
        if (optional.isPresent()) {
            token = optional.get();
        } else {
            token = tokenRepository.save(new Token(user.getUsername(), user.getUsername()));
        }
        return token.getToken();
    }

    public boolean verifyToken(String token) {
        Optional<Token> optional = tokenRepository.findById(token);
        if (optional.isPresent()) {
            return true;
        }else {
            return false;
        }
    }
}