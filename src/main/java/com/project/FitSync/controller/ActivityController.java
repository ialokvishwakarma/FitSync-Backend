package com.project.FitSync.controller;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.service.ActivityService;
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
    public ResponseEntity<ActivityResponse> addActivity(@RequestBody ActivityRequest activityRequest){
        return ResponseEntity.ok(activityService.add(activityRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ActivityResponse>> getActivities(@PathVariable Long id){
        return ResponseEntity.ok(activityService.findByUser(id));
    }
}
