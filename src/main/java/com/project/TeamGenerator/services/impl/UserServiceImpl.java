package com.project.TeamGenerator.services.impl;

import com.project.TeamGenerator.models.User;
import com.project.TeamGenerator.models.dto.UserDTO;
import com.project.TeamGenerator.repositories.UserRepository;
import com.project.TeamGenerator.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserDTO userDTO) throws Exception{
        if(userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty())
            throw new NullPointerException("Empty username");

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

        return "User was registered";
    }
}
