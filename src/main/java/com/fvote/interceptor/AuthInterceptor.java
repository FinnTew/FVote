package com.fvote.interceptor;

import com.fvote.entity.Users;
import com.fvote.service.UserService;
import com.fvote.util.JwtUtil;
import com.fvote.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        System.out.println(request.getHeader("Authorization"));
        if (request.getHeader("Authorization") == null) {
            response.setStatus(401);
            return false;
        }

        String token = request.getHeader("Authorization");
        if (!token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        token = token.strip().split(" ")[1];
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (NoSuchAlgorithmException e) {
            response.setStatus(401);
            return false;
        }
        if (claims == null) {
            response.setStatus(401);
            return false;
        }

        String username = claims.getSubject().split(":")[0];
        String finalToken = token;

        Optional<Users> user = userService.getUserByUsername(username);
        if (user.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        val redisTokenKey = "user_token:" + user.get().getId();
        if (!finalToken.equals(redisUtil.get(redisTokenKey))) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
