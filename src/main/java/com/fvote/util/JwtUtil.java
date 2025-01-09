package com.fvote.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 24*60 * 60 *1000L;// 60 * 60 *1000  一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "finntewvote";

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createJWT(String subject) throws NoSuchAlgorithmException {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    public static String createJWT(String subject, Long ttlMillis) throws NoSuchAlgorithmException {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) throws NoSuchAlgorithmException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .id(uuid)              //唯一的ID
                .subject(subject)   // 主题  可以是JSON数据
                .issuer("sg")     // 签发者
                .issuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .expiration(expDate);
    }

    public static String createJWT(String id, String subject, Long ttlMillis) throws NoSuchAlgorithmException {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static SecretKey generalKey() throws NoSuchAlgorithmException {
//        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedKey = digest.digest(JWT_KEY.getBytes());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
    }

    public static Claims parseJWT(String jwt) throws NoSuchAlgorithmException {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }


}