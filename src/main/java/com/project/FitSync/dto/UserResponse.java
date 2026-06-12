package com.project.FitSync.dto;

import com.project.FitSync.model.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
