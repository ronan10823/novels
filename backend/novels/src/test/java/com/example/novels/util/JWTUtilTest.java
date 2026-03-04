package com.example.novels.util;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.novels.member.utils.JWTUtil;

@SpringBootTest
public class JWTUtilTest {

    private JWTUtil jwtUtil;

    // 테스트 메소드 실행 시 공통적으로 처리해야 할 작업
    @BeforeEach
    public void testBefore() {
        System.out.println("------- JWT test ----------");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() {
        String email = "user11@gmail.com";
        String str = JWTUtil.generateToken(Map.of("email", email, "name", "User1"), 10);
        System.out.println(str);
    }

    @Test
    public void testValidate() throws InterruptedException {
        String email = "user11@gmail.com";
        String token = JWTUtil.generateToken(Map.of("email", email, "name", "User1"), 1);

        // 5초 대기
        Thread.sleep(5000);

        Map<String, Object> claimMap = JWTUtil.validateToken(token);

        System.out.println(claimMap);
    }
}
