package com.project.FitSync.exceptions;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String s) {
        super(s);
    }
}
