package org.fund.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.fund.model.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil implements Serializable {
    private static String secretKey;
    private static int expirationMinutes;
    private static final String claims_key = "userData";

    @Value("${jwt.secretKey}")
    public void setSecret(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${jwt.expirationMinutes}")
    public void setExpirationMinutes(int expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }

    public static String createToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(claims_key, user);
        return generateToken(claims);
    }

    public static Users getTokenData(String token) {
        Claims claims = extractAllClaims(token);
        return (Users) claims.get(claims_key);
    }

    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(DateUtils.addMinutes(new Date(), expirationMinutes))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private static String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public static boolean validateToken(String token, String subject) {
        return (subject.equals(extractSubject(token)) && !isTokenExpired(token));
    }

    @FunctionalInterface
    private static interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }

}
