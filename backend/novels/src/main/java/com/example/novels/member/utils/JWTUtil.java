package com.example.novels.member.utils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.management.RuntimeErrorException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTUtil {

    // signature
    private static String key = "1234567890123456789012345678901234567890";

    // 토큰 생성 메서드
    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey key = null;

        try {
            // HS256 방식의 암호화 키 생성
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("utf-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String jwtStr = Jwts.builder()
                .subject("JWT")
                .claims(valueMap)
                // 발급시간설정
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                // 만료시간설정
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
        return jwtStr;
    }

    // 토큰 검증 메서드
    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {

            SecretKey secretKey = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("utf-8"));

            claim = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return claim;
    }
}
