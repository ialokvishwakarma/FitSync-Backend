package com.project.FitSync.dto;

import com.project.FitSync.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {
    private Long userId;

    private int duration;
    private int caloriesBurned;


    private Instant startTime;
    private Instant endTime;


    private ActivityType type;
    private Map<String, Object> additionalMetrics;
}
