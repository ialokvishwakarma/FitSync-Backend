package com.project.FitSync.service;

import com.project.FitSync.dto.LoginRequest;
import com.project.FitSync.dto.LoginResponse;
import com.project.FitSync.dto.UserRequest;
import com.project.FitSync.dto.UserResponse;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.UserRepository;
import com.project.FitSync.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserResponse register(UserRequest userRequest) {
        User user = modelMapper.map(userRequest,User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }


    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) return null;
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) return null;

        String token = jwtUtils.generateTokenFromEmail(user.getEmail(),user.getRole().toString());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        modelMapper.map(user,loginResponse);
        return loginResponse;
    }

}
