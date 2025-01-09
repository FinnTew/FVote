package com.fvote.service;

import com.fvote.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void TestCreateUser() {
        Users user = new Users();
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@test.com");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        Users u = userService.createUser(user);
        System.out.println(u);
    }
}
