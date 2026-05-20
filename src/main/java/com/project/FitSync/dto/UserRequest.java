package com.project.FitSync.dto;

import com.project.FitSync.model.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
}
