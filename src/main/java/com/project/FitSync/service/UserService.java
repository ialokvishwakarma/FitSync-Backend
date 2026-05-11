package com.project.FitSync.service;

import com.project.FitSync.dto.UserRequest;
import com.project.FitSync.dto.UserResponse;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserResponse register(UserRequest userRequest) {
        User user = modelMapper.map(userRequest,User.class);
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }
}
