package com.project.FitSync.service;

import com.project.FitSync.dto.LoginRequest;
import com.project.FitSync.dto.LoginResponse;
import com.project.FitSync.dto.UserRequest;
import com.project.FitSync.dto.UserResponse;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.exceptions.WrongPasswordException;
import com.project.FitSync.model.User;
import com.project.FitSync.model.UserRole;
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
        UserRole role = userRequest.getRole() !=null ? userRequest.getRole() : UserRole.USER;
        User user = modelMapper.map(userRequest,User.class);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }


    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null){
            throw new UserNotFoundException("User not found with email : ",loginRequest.getEmail() );
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new WrongPasswordException("Password is not matched");
        }

        String token = jwtUtils.generateTokenFromEmail(user.getEmail(),user.getRole().toString());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        modelMapper.map(user,loginResponse);
        return loginResponse;
    }

}
