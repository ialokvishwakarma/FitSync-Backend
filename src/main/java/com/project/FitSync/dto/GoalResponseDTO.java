package com.project.FitSync.dto;

import com.project.FitSync.model.ActivityType;
import com.project.FitSync.model.GoalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponseDTO {
    private Long userId;
    private Long id;
    private String title;
    private String description;
    private ActivityType type;
    private GoalStatus status;
    private double targetValue;
    private double totalProgress;
    private LocalDate startDate;
    private LocalDate endDate;
}
