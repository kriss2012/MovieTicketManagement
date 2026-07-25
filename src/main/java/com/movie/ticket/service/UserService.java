package com.movie.ticket.service;

import java.util.List;
import com.movie.ticket.entity.User;

public interface UserService {
    User registerUser(User user);
    User authenticateUser(String email, String password);
    User authenticateAdmin(String email, String password);
    User findByEmail(String email);
    User findById(Long id);
    List<User> getAllUsers();
}
