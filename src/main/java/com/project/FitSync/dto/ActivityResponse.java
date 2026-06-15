package com.project.FitSync.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.FitSync.model.ActivityType;
import com.project.FitSync.model.Recommendation;
import com.project.FitSync.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {
    private Long userId;
    private Long id;

//    private UserResponse user;

    private int duration;
    private ActivityType type;
    private int caloriesBurned;
    private Instant startTime;
    private Instant endTime;
    private Map<String, Object> additionalMetrics;


}
