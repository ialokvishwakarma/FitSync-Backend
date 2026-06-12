package com.project.FitSync.dto;

import com.project.FitSync.model.ActivityType;
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
    private int duration;
    @Positive
    private int caloriesBurned;


    private Instant startTime;

    private Instant endTime;


    private ActivityType type;
    private Map<String, Object> additionalMetrics;
}
