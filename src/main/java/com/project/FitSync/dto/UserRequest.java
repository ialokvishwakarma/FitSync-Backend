package com.project.FitSync.dto;

import com.project.FitSync.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;

    @NotBlank(message = "Email Required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password Required")
    @Length(min = 6, message = "Password must be of minimum 6 characters")
    private String password;
    private UserRole role;
}
