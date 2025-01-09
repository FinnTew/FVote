package com.fvote.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class JwtUtilTest {

    @Test
    public void testGenerateToken() throws NoSuchAlgorithmException {
        String subject = "test";
        String token = JwtUtil.createJWT(subject);
        System.out.println(token);
    }

    @Test
    public void testVerifyToken() throws NoSuchAlgorithmException {
        String subject = "test";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0MzFjNjJmM2U1MTU0MzNkOTdhM2JlODVlYzIxNzllNCIsInN1YiI6InRlc3QiLCJpc3MiOiJzZyIsImlhdCI6MTczNDEwMTU0MSwiZXhwIjoxNzM0MTg3OTQxfQ.oJUDT-r7EByKb53cowcNaihD8mTEJTzXycreyC9imIM";
        System.out.println(JwtUtil.parseJWT(token));
    }
}
