package com.project.FitSync.service;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.dto.ActivityUpdateRequestDTO;
import com.project.FitSync.exceptions.AccessDeniedExceptionUser;
import com.project.FitSync.exceptions.ActivityNotFoundException;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.model.Activity;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public ActivityResponse add(ActivityRequest activityRequest, Authentication authentication ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Activity activity = modelMapper.map(activityRequest,Activity.class);
        activity.setId(null);
        activity.setUser(user);
        Activity savedActivity = activityRepository.save(activity);
        return modelMapper.map(savedActivity,ActivityResponse.class);
    }

    public List<ActivityResponse> findByUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        List<Activity> activities = activityRepository.findByUserId(user.getId());
        if(activities.isEmpty()){
            throw new ActivityNotFoundException("Activity not found with Id : ",user.getId());
        }

        return activities.stream()
                .map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .toList();

    }

    public void deleteActivityById(Long id,Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Activity activity = activityRepository.findById(id).orElseThrow(
                ()-> new ActivityNotFoundException("Activity not found with Id : ",id)
        );
        if(!activity.getUser().getId().equals(user.getId())){
            throw new AccessDeniedExceptionUser("Access Denied");
        }
                activityRepository.deleteById(user.getId());

    }

    public ActivityResponse updateActivity(Long id,ActivityUpdateRequestDTO updateRequestDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Activity activity = activityRepository.findById(id).orElseThrow(
                ()-> new ActivityNotFoundException("Activity not found with Id : ",id)
        );
        if(!activity.getUser().getId().equals(user.getId())){
            throw new AccessDeniedExceptionUser("Access Denied");
        }
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
