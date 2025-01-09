package com.fvote.service;

import com.fvote.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Users createUser(Users user);
    Users updateUser(Users user);
    Optional<Users> getUserById(Long id);
    Optional<Users> getUserByUsername(String username);
    Optional<Users> getUserByEmail(String email);
    List<Users> getAllUsers();
    void deleteUser(Long id);
}
