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
            System.out.println(1);
            response.setStatus(401);
            return false;
        }

        String token = request.getHeader("Authorization");
        if (!token.startsWith("Bearer ")) {
            System.out.println(2);
            response.setStatus(401);
            return false;
        }

        token = token.strip().split(" ")[1];
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(3);
            response.setStatus(401);
            return false;
        }
        if (claims == null) {
            System.out.println(4);
            response.setStatus(401);
            return false;
        }

        String username = claims.getSubject().split(":")[0];
        String finalToken = token;

        Optional<Users> user = userService.getUserByUsername(username);
        if (user.isEmpty()) {
            System.out.println(5);
            response.setStatus(401);
            return false;
        }

        val redisTokenKey = "user_token:" + user.get().getId();
        if (!finalToken.equals(redisUtil.get(redisTokenKey))) {
            System.out.println(6);
            response.setStatus(401);
            return false;
        }
        System.out.println(7);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
