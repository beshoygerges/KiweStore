package com.kiwie.store.service;

import com.kiwie.store.entity.User;

import java.util.UUID;

public interface JwtService {

    String generateToken(User user, UUID sessionId);

    boolean isTokenValid(String token, User user);

    String getUsername(String token);
}
