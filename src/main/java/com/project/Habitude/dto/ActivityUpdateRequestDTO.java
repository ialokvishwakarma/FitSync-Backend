package com.project.Habitude.dto;

import com.project.Habitude.model.ActivityCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUpdateRequestDTO {
    @Positive
    private Integer duration;
    @Positive
    private Integer caloriesBurned;
    @Positive
    private int distance;

    @NotNull
    private Long focusId;

}
