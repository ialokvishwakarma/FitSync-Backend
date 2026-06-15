package com.project.FitSync.controller;

import com.project.FitSync.dto.GoalRequestDTO;
import com.project.FitSync.dto.GoalResponseDTO;
import com.project.FitSync.dto.GoalStatusRequest;
import com.project.FitSync.dto.UpdateGoalRequest;
import com.project.FitSync.exceptions.AccessDeniedExceptionUser;
import com.project.FitSync.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    @PostMapping("/add")
    public ResponseEntity<GoalResponseDTO> addGoal(@Valid
                                                       Authentication authentication,
                                                       @RequestBody GoalRequestDTO requestDTO) {
        return ResponseEntity.ok(goalService.createGoal(authentication,requestDTO));
    }

    @GetMapping("/my-goals")
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals(
            @Valid
            Authentication authentication
    ){
        return ResponseEntity.ok(goalService.getAllGoals(authentication));
    }

   @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGoal(@Valid
                                           Authentication authentication,
                                           Long id){
        goalService.delete(authentication,id);
        return ResponseEntity.noContent().build();
   }

   @PutMapping("/update/{id}")
    public ResponseEntity<GoalResponseDTO> updateGoal(@Valid
                                                         @PathVariable  Long id,
                                                      @RequestBody UpdateGoalRequest request,
                                                      Authentication authentication){
        return ResponseEntity.ok(goalService.update(id,request,authentication));
   }

   @PatchMapping("/status/{id}")
    public ResponseEntity<GoalResponseDTO> updateStatus(
            @Valid
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody GoalStatusRequest statusRequest
   ){
        return ResponseEntity.ok(goalService.updateStatus(id,statusRequest,authentication));
   }

}
