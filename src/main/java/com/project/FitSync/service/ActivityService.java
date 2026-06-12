package com.project.FitSync.service;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.dto.ActivityUpdateRequestDTO;
import com.project.FitSync.exceptions.ActivityNotFoundException;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.model.Activity;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
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
        User user = userRepository.findById(activityRequest.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User not found with Id : ",activityRequest.getUserId())
        );
        Activity activity = modelMapper.map(activityRequest,Activity.class);
        activity.setId(null);
        activity.setUser(user);
        System.out.println(activity);
        Activity savedActivity = activityRepository.save(activity);
        return modelMapper.map(savedActivity,ActivityResponse.class);
    }

    public List<ActivityResponse> findByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User not found with Id : ",id)
        );
        List<Activity> activities = activityRepository.findByUserId(id);
        if(activities.isEmpty()){
            throw new ActivityNotFoundException("Activity not found with Id : ",id);
        }

        return activities.stream()
                .map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .toList();

    }

    public void deleteActivityById(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(
                ()-> new ActivityNotFoundException("Activity not found with Id : ",id)
        );
                activityRepository.deleteById(id);

    }

    public ActivityResponse updateActivity(ActivityUpdateRequestDTO updateRequestDTO, Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(
                ()-> new ActivityNotFoundException("Activity not found with Id : ",id)
        );

        activity.setDuration(updateRequestDTO.getDuration());
        activity.setType(updateRequestDTO.getType());
        activity.setStartTime(updateRequestDTO.getStartTime());
        activity.setCaloriesBurned(updateRequestDTO.getCaloriesBurned());
        activity.setEndTime(updateRequestDTO.getEndTime());
        activity.setAdditionalMetrics(updateRequestDTO.getAdditionalMetrics());

        Activity updated = activityRepository.save(activity);
        return modelMapper.map(updated,ActivityResponse.class);
    }
}
