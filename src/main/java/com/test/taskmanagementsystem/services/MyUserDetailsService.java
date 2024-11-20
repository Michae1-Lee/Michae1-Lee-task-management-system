package com.test.taskmanagementsystem.services;

import com.test.taskmanagementsystem.configs.MyUserDetails;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Neil Alishev
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(s);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new MyUserDetails(user.get());
    }
}
