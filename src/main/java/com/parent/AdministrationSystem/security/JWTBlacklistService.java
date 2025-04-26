package com.parent.AdministrationSystem.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class JWTBlacklistService {
    private Set<String> blacklistedTokens = new HashSet<>();

    // Add token to blacklist
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Check if the token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
