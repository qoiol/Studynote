package com.example.postservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenUtils {

    public static String getUserId(String token, String key) {
        return extractClaims(token, key).get("userId", String.class);
    }

    public static String role(String token, String key) {
        return extractClaims(token, key).get("role", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiration = extractClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    private static Claims extractClaims(String token, String key) throws ExpiredJwtException {
//        return Jwts.parser().setSigningKey(getKey(key)).setAllowedClockSkewSeconds(1)
//                .build().parseClaimsJws(token).getBody();
        return Jwts.parser().clockSkewSeconds(1)
                .verifyWith(getKey(key))
                .decryptWith(getKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static String generateToken(String userId, String userType, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims().add("userId", userId).add("role", userType).build();

        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredTimeMs))
                .signWith(getKey(key), Jwts.SIG.HS256)
                .compact();
    }

    public static SecretKey getKey(String key) {
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

//    public static SecretKey getSecretKey(String key) {
//        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(bytes);
//    }


    public static Cookie addCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        return cookie;
    }

    public static void removeCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
