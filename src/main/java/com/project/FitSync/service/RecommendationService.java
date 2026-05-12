package com.project.FitSync.service;

import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.dto.RecommendationRequest;
import com.project.FitSync.dto.RecommendationResponse;
import com.project.FitSync.model.Activity;
import com.project.FitSync.model.Recommendation;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.repository.RecommendationRepository;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final RecommendationRepository recommendationRepository;
    private final ModelMapper modelMapper;


    public ResponseEntity<Recommendation> generateRecommendation(RecommendationRequest recommendationRequest){
            User user = userRepository.findById(recommendationRequest.getUserId()).orElseThrow();

            Activity activity = activityRepository.findById(recommendationRequest.getActivityId()).orElseThrow();
            Recommendation recommendation = new Recommendation();
            recommendation.setUser(user);
            recommendation.setActivity(activity);
            recommendation.setRecommendations(recommendationRequest.getReccomendations());
            recommendation.setSuggestions(recommendationRequest.getSuggestions());
            recommendation.setImprovements(recommendationRequest.getImprovements());
            recommendation.setSafety(recommendationRequest.getSafety());

            return ResponseEntity.ok(recommendationRepository.save(recommendation));
    }

    public List<RecommendationResponse> getRecommendationByUser(Long userId) {
            List<Recommendation> recommendation = recommendationRepository.findByUserId(userId);
            return toResponseList(recommendation);
    }

    public List<RecommendationResponse> getRecommendationByActivity(Long activityId) {
        List<Recommendation> recommendation = recommendationRepository.findByActivityId(activityId);
        return toResponseList(recommendation);
    }

    public RecommendationResponse toResponse(Recommendation recommendation){
        return RecommendationResponse.builder()
                .userId(recommendation.getUser().getId())
                .activityId(recommendation.getActivity().getId())
                .activityType(String.valueOf(recommendation.getActivity().getType()))
                .recommendations(recommendation.getRecommendations())
                .suggestions(recommendation.getSuggestions())
                .improvements(recommendation.getImprovements())
                .safety(recommendation.getSafety())
                .createdAt(recommendation.getCreatedAt())
                .updatedAt(recommendation.getUpdatedAt())
                .build();

    }
    public List<RecommendationResponse> toResponseList(List<Recommendation> recommendations) {
        return recommendations.stream()
                .map(this::toResponse)
                .toList();
    }
}
