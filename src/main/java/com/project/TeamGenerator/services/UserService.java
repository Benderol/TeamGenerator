package com.project.TeamGenerator.services;

import com.project.TeamGenerator.models.dto.UserDTO;

public interface UserService {
    String registerUser(UserDTO userDTO) throws Exception;
}
