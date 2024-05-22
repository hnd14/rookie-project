package com.example.project.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.project.users.entities.User;

@Component
public class TokenProvider {
    private final Algorithm algorithm;

    public TokenProvider (@Value("${security.jwt.token.secret-key}") String jwtSecret) {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
    }

    public String generateAccessToken(User user){
        try{
            return JWT.create()
            .withClaim("sub", user.getUsername())
            .withExpiresAt(generateExpireDate())
            .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try{
            return JWT.require(algorithm)
            .build()
            .verify(token)
            .getSubject();
        }
        catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    private Instant generateExpireDate(){
        return Instant.now().plusSeconds(2*3600);
    }
}
