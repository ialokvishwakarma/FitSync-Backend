package com.project.FitSync.dto;

import com.project.FitSync.model.ActivityType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {
    @Positive
    private int duration;
    @Positive
    private int caloriesBurned;

    @NotNull
    private Instant startTime;
    @NotNull
    private Instant endTime;

    @NotNull
    private ActivityType type;
    private Map<String, Object> additionalMetrics;
}
