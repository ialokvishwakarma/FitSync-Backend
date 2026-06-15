package com.project.FitSync.controller;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.dto.ActivityUpdateRequestDTO;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {


    private final ActivityService activityService;

    @PostMapping("/track")
    public ResponseEntity<ActivityResponse> addActivity(@Valid
                                                            @RequestBody ActivityRequest activityRequest,
                                                            Authentication authentication
                                                        ){
        return ResponseEntity.ok(activityService.add(activityRequest,authentication));
    }

    @GetMapping("/my-activities")
    public ResponseEntity<List<ActivityResponse>> getActivities(@Valid
                                                                Authentication authentication){
        return ResponseEntity.ok(activityService.findByUser(authentication));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivity(@Valid @PathVariable Long id, Authentication authentication){
        activityService.deleteActivityById(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(@Valid
                                                               @PathVariable Long id,
                                                               @RequestBody ActivityUpdateRequestDTO updateRequestDTO,
                                                           Authentication authentication){
        return ResponseEntity.ok(activityService.updateActivity(id,updateRequestDTO, authentication));
    }
}
