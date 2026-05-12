package com.project.FitSync.controller;

import com.project.FitSync.dto.RecommendationRequest;
import com.project.FitSync.dto.RecommendationResponse;
import com.project.FitSync.model.Recommendation;
import com.project.FitSync.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<Recommendation> generateRecommendation(@RequestBody RecommendationRequest recommendationRequest){
        return (recommendationService.generateRecommendation(recommendationRequest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendationByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(recommendationService.getRecommendationByUser(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendationByActivityId(@PathVariable Long activityId){
        return ResponseEntity.ok(recommendationService.getRecommendationByActivity(activityId));
    }

}
