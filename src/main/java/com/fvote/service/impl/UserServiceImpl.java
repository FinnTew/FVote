package com.fvote.service.impl;

import com.fvote.entity.Users;
import com.fvote.repository.UsersRepository;
import com.fvote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Users createUser(Users user) {
        if (user.getId() != null) {
            throw new RuntimeException("Cannot create a new user with designated id");
        }
        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(Users user) {
        if (user.getId() == null) {
            throw new RuntimeException("Cannot update a user without an id");
        }
        Optional<Users> existingUser = usersRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }
        Users updatedUser = existingUser.get();
        updatedUser.setUsername(user.getUsername() != null ? user.getUsername() : updatedUser.getUsername());
        updatedUser.setPassword(user.getPassword() != null ? user.getPassword() : updatedUser.getPassword());
        updatedUser.setEmail(user.getEmail() != null ? user.getEmail() : updatedUser.getEmail());
        updatedUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return usersRepository.save(updatedUser);
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    @Override
    public Optional<Users> getUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        } else {
            usersRepository.deleteById(id);
        }
    }
}
