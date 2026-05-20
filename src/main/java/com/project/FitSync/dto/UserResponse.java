package com.project.FitSync.dto;

import com.project.FitSync.model.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

}
