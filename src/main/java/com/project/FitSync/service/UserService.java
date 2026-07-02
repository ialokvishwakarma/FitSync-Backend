package com.project.FitSync.service;

import com.project.FitSync.dto.LoginRequest;
import com.project.FitSync.dto.LoginResponse;
import com.project.FitSync.dto.UserRequest;
import com.project.FitSync.dto.UserResponse;
import com.project.FitSync.exceptions.UserAlreadyExistsException;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.exceptions.WrongPasswordException;
import com.project.FitSync.model.AuthProviderType;
import com.project.FitSync.model.RefreshToken;
import com.project.FitSync.model.User;
import com.project.FitSync.model.UserRole;
import com.project.FitSync.repository.RefreshTokenRepository;
import com.project.FitSync.repository.UserRepository;
import com.project.FitSync.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserResponse register(UserRequest userRequest) {
        UserRole role = userRequest.getRole() !=null ? userRequest.getRole() : UserRole.USER;
        User userCheck = userRepository.findByEmail(userRequest.getEmail());
        if(userCheck!=null){
        throw new UserAlreadyExistsException("User already exists with  this email : ");
        }
        User user = modelMapper.map(userRequest, User.class);
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
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        if(refreshToken==null) refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        refreshTokenRepository.delete(refreshToken);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        String token = jwtUtils.generateTokenFromEmail(user.getEmail(),user.getRole().toString());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(newRefreshToken.getToken());
        modelMapper.map(user,loginResponse);
        return loginResponse;
    }

    public LoginResponse handleOauth2LoginRequest(OAuth2User oAuth2User, String registrationId){
//        AuthProviderType providerType = jwtUtils.getProviderTypeFromRegistrationId(registrationId);
//        String providerId = jwtUtils.getProviderIdFromOAuth2User(oAuth2User, registrationId);

//        User user = userRepository.fingByProviderIdAndProviderType(providerId,providerType).orElse(null);
        String email = oAuth2User.getAttribute("email");

        User existingUser = userRepository.findByEmail(email);
        if(existingUser == null){
            existingUser = new User();
            existingUser.setEmail(email);
            existingUser.setRole(UserRole.USER);
            existingUser.setFirstName(oAuth2User.getAttribute("name"));
            existingUser.setProviderType(AuthProviderType.GOOGLE);
            userRepository.save(existingUser);
        }

        return createLoginResponse(existingUser);
    }

    private LoginResponse createLoginResponse(User user){
        String accessToken = jwtUtils.generateTokenFromEmail(user.getEmail(), String.valueOf(user.getRole()));
        RefreshToken refreshObject = refreshTokenService.createRefreshToken(user.getEmail());
        String refreshToken = refreshObject.getToken();
        LoginResponse response = new LoginResponse();
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;

    }
}
