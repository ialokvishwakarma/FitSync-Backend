package com.project.FitSync.dto;

import com.project.FitSync.model.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GoalStatusRequest {
    private GoalStatus status;
}
