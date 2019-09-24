package com.hl.springboot_shiro_jwt.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.hl.springboot_shiro_jwt.domain.Token;
import com.hl.springboot_shiro_jwt.domain.User;
import com.hl.springboot_shiro_jwt.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtConfig {

    /**
     * JWT 自定义密钥 这里写死
     */
    private static final String SECRET_KEY = "123456789012345abcd";
    /**
     * JWT 过期时间值 这里写死 720个小时
     */
    private static final long EXPIRE_TIME = 2592000;

    @Autowired
    private TokenRepository tokenRepository;

    public String createToken(User user, Long exTime) {
        String jwtId = UUID.randomUUID().toString(); //JWT 随机ID,做为验证的key
        //1 . 加密算法进行签名得到token
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String token = JWT.create()
                .withClaim("username", user.getUsername())
                .withClaim("userId", user.getId())
                .withClaim("jwt-id", jwtId)
                .withExpiresAt(new Date(System.currentTimeMillis() + (exTime == null ? EXPIRE_TIME * 1000 : exTime * 1000)))  //JWT 配置过期时间的正确姿势
                .sign(algorithm);

        tokenRepository.save(new Token(jwtId, token));
        return token;
    }

    public boolean verifyToken(String token) {
        try {
            Optional<Token> optional = tokenRepository.findById(getJwtIdByToken(token));
            if (!optional.isPresent()) {
                return false;
            }
            String cacheToken = optional.get().getToken();
            if (!cacheToken.equals(token)) {
                return false;
            }

            //2 . 得到算法相同的JWTVerifier
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", getUsernameByToken(cacheToken))
                    .withClaim("userId", getUserIdByToken(cacheToken))
                    .withClaim("jwt-id", getJwtIdByToken(cacheToken))
                    .acceptExpiresAt(System.currentTimeMillis() + EXPIRE_TIME * 1000)  //JWT 正确的配置续期姿势
                    .build();
            //3 . 验证token
            verifier.verify(cacheToken);
            tokenRepository.save(new Token(getJwtIdByToken(cacheToken), token));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getJwtIdByToken(String token) {
        return JWT.decode(token).getClaim("jwt-id").asString();
    }

    public String getUsernameByToken(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }

    public String getUserIdByToken(String token) {
        return JWT.decode(token).getClaim("userId").asString();
    }
}