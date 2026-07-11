package com.project.FitSync.service;

import com.project.FitSync.dto.ActivityRequest;
import com.project.FitSync.dto.ActivityResponse;
import com.project.FitSync.dto.ActivityUpdateRequestDTO;
import com.project.FitSync.exceptions.AccessDeniedExceptionUser;
import com.project.FitSync.exceptions.ActivityNotFoundException;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.model.*;
import com.project.FitSync.repository.ActivityRepository;
import com.project.FitSync.repository.GoalRepository;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final GoalRepository goalRepository;


    public ActivityResponse add(ActivityRequest activityRequest, Authentication authentication ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Activity activity = modelMapper.map(activityRequest,Activity.class);
        activity.setId(null);
        activity.setUser(user);
        Activity savedActivity = activityRepository.save(activity);
        updateGoalProgress(user.getId(), savedActivity.getType());
        log.info("Activity added successfully for user: {}",authentication.getName());
        return modelMapper.map(savedActivity,ActivityResponse.class);
    }

    public List<ActivityResponse> findByUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        List<Activity> activities = activityRepository.findByUserId(user.getId());
        if(activities.isEmpty()){
            log.warn("Activity not present for user: {}",authentication.getName());
            throw new ActivityNotFoundException("Activity not found with Id : ",user.getId());
        }

        log.info("Activity returned successfully for user: {}",authentication.getName());
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
        log.info("Activity deleted with id: {}",id);
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
        updateGoalProgress(user.getId(), updated.getType());
        return modelMapper.map(updated,ActivityResponse.class);
    }

    public void updateGoalProgress(Long userId, ActivityType type){
        List<Goal> goals = goalRepository.findByUserIdAndType(userId,type);

        double totalProgress = switch (type){
            case RUNNING, WALKING, CYCLING -> activityRepository.sumDistanceByUserIdAndType(userId,type);
            case YOGA, WEIGHT_TRAINING, STRETCHING, SWIMMING, CARDIO, OTHER -> activityRepository.sumCaloriesByUserIdAndType(userId,type);
            default ->  activityRepository.sumDurationByUserIdAndType(userId,type);
        };
        for (Goal goal : goals){
            goal.setTotalProgress(totalProgress);
            if(totalProgress>= goal.getTargetValue()) goal.setStatus(GoalStatus.DONE);
            else goal.setStatus(GoalStatus.IN_PROGRESS);
            goalRepository.save(goal);
        }
    }
}
