package com.artSoft.bankChecks.service.user;

import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String getSecretKey();

    int getCookieExpiration();

    long getJwtExpiration();

    long getRefreshExpiration();

    String extractUsername(String token);

    Claims extractClaim(String token);

    <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String buildToken(Map<String, Object> extraClaims, @NotNull UserDetails userDetails, long expiration);

    boolean isTokenValid(String token, @NotNull UserDetails userDetails);
}
