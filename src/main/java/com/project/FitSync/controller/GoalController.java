package com.project.FitSync.controller;

import com.project.FitSync.dto.GoalRequestDTO;
import com.project.FitSync.dto.GoalResponseDTO;
import com.project.FitSync.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    @PostMapping("/add")
    public ResponseEntity<GoalResponseDTO> addGoal(@Valid @RequestBody GoalRequestDTO requestDTO){
        return ResponseEntity.ok(goalService.createGoal(requestDTO));
            }
}
