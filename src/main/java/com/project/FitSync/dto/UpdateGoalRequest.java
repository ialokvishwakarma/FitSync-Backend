package com.project.FitSync.dto;

import com.project.FitSync.model.ActivityType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGoalRequest {
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
