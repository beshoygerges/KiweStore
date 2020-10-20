package com.kiwie.store.service.impl;

import com.kiwie.store.entity.User;
import com.kiwie.store.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Override
    public String generateToken(User user, UUID sessionId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUsername(), sessionId);
    }

    public String getSessionId(String token) {
        return getClaim(token, Claims::getId);
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        return !isTokenExpired(token) && getUsername(token).equals(user.getUsername())
                && user.getUserSessions()
                .stream()
                .anyMatch(s -> s.getSessionId().equals(UUID.fromString(getSessionId(token))) && s.isEnabled() && !s.isExpired());
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    @Override
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = getClaims(token);
        return claimsTFunction.apply(claims);
    }

    private String createToken(Map<String, Object> claims, String subject, UUID sessionId) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(sessionId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiration))
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
    }
}
