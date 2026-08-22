package com.project.Habitude.dto;

import com.project.Habitude.model.ActivityCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse implements Serializable {
    private Long userId;
    private Long id;
    private String title;
    private String description;
    private String focusName;
    private Double value;
    private String customUnit;
    private Instant activityDateTime;
    private String habitTitle;
}
