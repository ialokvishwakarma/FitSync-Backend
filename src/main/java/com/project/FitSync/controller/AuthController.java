package com.project.FitSync.controller;


import com.project.FitSync.dto.*;
import com.project.FitSync.model.RefreshToken;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.RefreshTokenRepository;
import com.project.FitSync.security.JwtUtils;
import com.project.FitSync.service.RefreshTokenService;
import com.project.FitSync.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
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

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(
            @RequestBody RefreshRequest refreshRequest
            ){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshRequest.getRefreshToken()).orElseThrow();
//        String refreshToken = refreshRequest.getRefreshToken();
        if(refreshTokenService.isExpired(refreshToken)){
            refreshTokenRepository.delete(refreshToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwtToken = jwtUtils.generateTokenFromEmail(refreshToken.getUser().getEmail(),refreshToken.getUser().getRole().name());
        RefreshResponse response = new RefreshResponse();
        response.setAccessToken(jwtToken);
        return ResponseEntity.ok(response);
    }

    // for testing ADMIN role access
    @GetMapping("/admin/dashboard")
    public String dashBoard(){
        return "At the dashboard page";
    }

}
