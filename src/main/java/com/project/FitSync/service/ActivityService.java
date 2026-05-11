package com.project.FitSync.service;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.model.Activity;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public ActivityResponse add(ActivityRequest activityRequest) {
        User user = userRepository.findById(activityRequest.getUserId()).orElseThrow(()-> new RuntimeException("User not found :"));
        Activity activity = modelMapper.map(activityRequest,Activity.class);
        activity.setId(null);
        activity.setUser(user);
        System.out.println(activity);
        Activity savedActivity = activityRepository.save(activity);
        return modelMapper.map(savedActivity,ActivityResponse.class);
    }

    public List<ActivityResponse> findByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        List<Activity> activities = activityRepository.findByUserId(id);

        return activities.stream()
                .map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .toList();

    }
}
