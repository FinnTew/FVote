package com.fvote.controller;

import com.fvote.entity.*;
import com.fvote.service.UserService;
import com.fvote.util.JwtUtil;
import com.fvote.util.RedisUtil;
import com.fvote.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public Result<?> login(@RequestBody UserLoginReq req) throws NoSuchAlgorithmException {
        if (req.getUsername().length() < 8) {
            return Result.error("Username must be at least 8 characters");
        }
        if (req.getPassword().length() < 8) {
            return Result.error("Password must be at least 8 characters");
        }

        Users userFromDB = userService.getUserByUsername(req.getUsername()).orElse(null);
        if (userFromDB == null) {
            return Result.error("User not found");
        }
        if (!userFromDB.getPassword().equals(req.getPassword())) {
            return Result.error("Password incorrect");
        }

        String uuid = JwtUtil.getUUID();
        String tokenSubject = userFromDB.getUsername() + ":" + uuid;
        String token = JwtUtil.createJWT(tokenSubject);

        String redisTokenKey = "user_token:" + userFromDB.getId();
        redisUtil.set(redisTokenKey, token);

        UserLoginResp resp = new UserLoginResp();
        resp.setId(userFromDB.getId());
        resp.setUsername(userFromDB.getUsername());
        resp.setEmail(userFromDB.getEmail());
        resp.setToken(token);

        return Result.success(
                "Login success",
                resp
        );
    }

    @PostMapping("/registry")
    public Result<?> registry(@RequestBody UserRegisterReq req) throws NoSuchAlgorithmException {
        if (req.getUsername().length() < 8) {
            return Result.error("Username must be at least 8 characters");
        }
        if (req.getPassword().length() < 8) {
            return Result.error("Password must be at least 8 characters");
        }
        if (!Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$")
                .matcher(req.getEmail())
                .matches()
        ) {
            return Result.error("Invalid email address");
        }

        if (userService.getUserByUsername(req.getUsername()).isPresent()) {
            return Result.error("Username already exists");
        }
        if (userService.getUserByEmail(req.getEmail()).isPresent()) {
            return Result.error("Email already exists");
        }

        Users user = new Users();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Users userCreated = userService.createUser(user);

        String uuid = JwtUtil.getUUID();
        String tokenSubject = userCreated.getUsername() + ":" + uuid;
        String token = JwtUtil.createJWT(tokenSubject);

        String redisTokenKey = "user_token:" + userCreated.getId();
        redisUtil.set(redisTokenKey, token);

        UserLoginResp resp = new UserLoginResp();
        resp.setId(userCreated.getId());
        resp.setUsername(userCreated.getUsername());
        resp.setEmail(userCreated.getEmail());
        resp.setToken(token);

        return Result.success(
                "Register and login success",
                resp
        );
    }

    @GetMapping("/verify")
    public Result<?> verify(HttpServletRequest request) throws NoSuchAlgorithmException {
        String token = request.getHeader("Authorization");
        if (token == null) {
            return Result.error("Token not found");
        }

        if (!token.startsWith("Bearer ")) {
            return Result.error("Invalid token");
        }

        token = token.strip().split(" ")[1];
        Claims claims = JwtUtil.parseJWT(token);
        if (claims == null) {
            return Result.error("Invalid token");
        }

        String subject = claims.getSubject();
        String[] subArray = subject.split(":");

        String username = subArray[0];
        Users userFromDB = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        UserLoginResp resp = new UserLoginResp();
        resp.setId(userFromDB.getId());
        resp.setUsername(userFromDB.getUsername());
        resp.setEmail(userFromDB.getEmail());
        resp.setToken(token);

        return Result.success(
                "Token verified successfully",
                resp
        );
    }

    @GetMapping("refresh")
    public Result<?> refresh(HttpServletRequest request) throws NoSuchAlgorithmException {
        String token = request.getHeader("Authorization");
        if (token == null) {
            return Result.error("Token not found");
        }

        if (!token.startsWith("Bearer ")) {
            return Result.error("Invalid token");
        }

        token = token.strip().split(" ")[1];
        Claims claims = JwtUtil.parseJWT(token);
        if (claims == null) {
            return Result.error("Invalid token");
        }

        String subject = claims.getSubject();
        String[] subArray = subject.split(":");

        String username = subArray[0];
        Users userFromDB = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        String uuid = JwtUtil.getUUID();
        String tokenSubject = userFromDB.getUsername() + ":" + uuid;
        String newToken = JwtUtil.createJWT(tokenSubject);

        String redisTokenKey = "user_token:" + userFromDB.getId();
        redisUtil.set(redisTokenKey, newToken);

        UserLoginResp resp = new UserLoginResp();
        resp.setId(userFromDB.getId());
        resp.setUsername(userFromDB.getUsername());
        resp.setEmail(userFromDB.getEmail());
        resp.setToken(newToken);

        return Result.success(
                "Token refreshed successfully",
                resp
        );
    }

    @GetMapping("/logout")
    public Result<?> logout(HttpServletRequest request) throws NoSuchAlgorithmException {
        String token = request.getHeader("Authorization");
        if (token == null) {
            return Result.error("Token not found");
        }

        if (!token.startsWith("Bearer ")) {
            return Result.error("Invalid token");
        }

        token = token.strip().split(" ")[1];
        Claims claims = JwtUtil.parseJWT(token);
        String subject = claims.getSubject();
        String[] subArray = subject.split(":");

        String username = subArray[0];
        Users userFromDB = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        redisUtil.delete("user_token:" + userFromDB.getId());

        return Result.success();
    }

    @PostMapping("/forget")
    public Result<?> forget(@RequestBody UserForgetPwdReq req) {
        if (req.getUsername().length() < 8) {
            return Result.error("Username must be at least 8 characters");
        }
        if (req.getOldPassword().length() < 8 || req.getNewPassword().length() < 8) {
            return Result.error("Password must be at least 8 characters");
        }

        Users userFromDB = userService.getUserByUsername(req.getUsername()).orElse(null);
        if (userFromDB == null) {
            return Result.error("User not found");
        }

        if (!userFromDB.getPassword().equals(req.getOldPassword())) {
            return Result.error("Old password incorrect");
        }

        userFromDB.setPassword(req.getNewPassword());
        userFromDB.setUpdatedAt(new Date());
        Users userUpdated = userService.updateUser(userFromDB);

        UserForgetPwdResp resp = new UserForgetPwdResp();
        resp.setUserId(userUpdated.getId());

        return Result.success(
                "Password updated successfully",
                resp
        );
    }
}
