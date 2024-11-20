package com.test.taskmanagementsystem.services;

import com.test.taskmanagementsystem.dto.UserDto;
import com.test.taskmanagementsystem.exceptions.UserAlreadyExists;
import com.test.taskmanagementsystem.exceptions.UserNotFound;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
    }

    public void saveUser(User user) {
        try{
            getUserByUsername(user.getUsername());
            throw new UserAlreadyExists();
        }
        catch (UserNotFound e){
            userRepository.save(user);
        }
    }

    public User register(UserDto user, String role) {
        user.setPassword(encoder.encode(user.getPassword()));
        User userr = new User();
        userr.setUsername(user.getUsername());
        userr.setPassword(user.getPassword());
        //role == "USER" или "ADMIN"
        userr.setRole(role.toUpperCase());
        saveUser(userr);
        return userr;
    }

}
