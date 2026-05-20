package com.project.FitSync.controller;


import com.project.FitSync.dto.LoginRequest;
import com.project.FitSync.dto.LoginResponse;
import com.project.FitSync.dto.UserRequest;
import com.project.FitSync.dto.UserResponse;
import com.project.FitSync.model.User;
import com.project.FitSync.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = userService.login(loginRequest);
        if(response==null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(response);
    }

    // for testing ADMIN role access
    @GetMapping("/admin/dashboard")
    public String dashBoard(){
        return "At the dashboard page";
    }



}
