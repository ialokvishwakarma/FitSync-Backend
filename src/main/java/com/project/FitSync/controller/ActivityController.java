package com.project.FitSync.controller;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.dto.ActivityUpdateRequestDTO;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {


    private final ActivityService activityService;

    @PostMapping("/track")
    public ResponseEntity<ActivityResponse> addActivity(@Valid @RequestBody ActivityRequest activityRequest){
        return ResponseEntity.ok(activityService.add(activityRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ActivityResponse>> getActivities(@PathVariable Long id){
        return ResponseEntity.ok(activityService.findByUser(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id){
        activityService.deleteActivityById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(@Valid @RequestBody ActivityUpdateRequestDTO updateRequestDTO, @PathVariable Long id){
        return ResponseEntity.ok(activityService.updateActivity(updateRequestDTO, id));
    }
}
