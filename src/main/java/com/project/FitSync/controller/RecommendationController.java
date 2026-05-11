package com.project.FitSync.controller;

import com.project.FitSync.dto.RecommendationRequest;
import com.project.FitSync.dto.RecommendationResponse;
import com.project.FitSync.model.Recommendation;
import com.project.FitSync.service.RecommendationService;
import lombok.Getter;
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendation(@PathVariable Long userId){
        return ResponseEntity.ok(recommendationService.getRecommendation(userId));
    }
}
