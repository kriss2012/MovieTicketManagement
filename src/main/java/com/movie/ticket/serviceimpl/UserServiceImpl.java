package com.movie.ticket.serviceimpl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.movie.ticket.entity.User;
import com.movie.ticket.repository.UserRepository;
import com.movie.ticket.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered.");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        String cleanEmail = email.trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(cleanEmail).orElse(null);
        if (user != null && password.trim().equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User authenticateAdmin(String email, String password) {
        User user = authenticateUser(email, password);
        if (user != null && user.isAdmin()) {
            return user;
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) return null;
        return userRepository.findByEmailIgnoreCase(email.trim().toLowerCase()).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
