package com.project.Habitude.dto;

import com.project.Habitude.model.ActivityCategory;
import com.project.Habitude.model.GoalStatus;
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
public class UpdateGoalRequest {
    @Length(max = 15)
    private String title;
    @Length(min = 20, max = 100)
    private String description;
    @NotNull
    private Long focusId;

    private GoalStatus goalStatus;

    @Positive
    private Double targetValue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
