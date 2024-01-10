package com.project.TeamGenerator.services.impl;

import com.project.TeamGenerator.models.User;
import com.project.TeamGenerator.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username - " + username + " was not found"));

        return org.springframework.security.core.userdetails.User
                .builder().username(username)
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
