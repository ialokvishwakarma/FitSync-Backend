package com.project.FitSync.dto;

import com.project.FitSync.model.ActivityType;
import com.project.FitSync.model.GoalStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalRequestDTO {
    @Length(max = 15)
    private String title;

    @Length(min = 20, max = 100)
    private String description;

    @NotNull
    private ActivityType type;


    @Positive
    private double targetValue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
