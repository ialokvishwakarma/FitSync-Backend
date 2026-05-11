package com.project.FitSync.controller;


import com.project.FitSync.dto.UserRequest;
import com.project.FitSync.dto.UserResponse;
import com.project.FitSync.model.User;
import com.project.FitSync.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.register(userRequest));
    }



}
