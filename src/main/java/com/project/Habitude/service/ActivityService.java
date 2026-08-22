package com.project.Habitude.service;

import com.project.Habitude.dto.ActivityRequest;
import com.project.Habitude.dto.ActivityResponse;
import com.project.Habitude.dto.ActivityUpdateRequestDTO;
import com.project.Habitude.exceptions.AccessDeniedExceptionUser;
import com.project.Habitude.exceptions.ActivityNotFoundException;
import com.project.Habitude.model.*;
import com.project.Habitude.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final GoalRepository goalRepository;
    private final HabitRepository habitRepository;
    private final FocusRepository focusRepository;


//    @CacheEvict(value = "activities", allEntries = true)
    public ActivityResponse add(ActivityRequest activityRequest, Authentication authentication ) {
        log.info("Entry");
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Habit habit = habitRepository.findById(activityRequest.getHabitId()).orElse(null);
        Focus focus = focusRepository.findById(activityRequest.getFocusId()).orElse(null);
        String habitTitle = "";
        String focusName = "";
        if(habit!=null) {
            habitTitle = habit.getTitle();
        }
        if(focus!=null){
            focusName = focus.getName();
        }
        String customUnit = activityRequest.getCustomUnit();
        customUnit  = customUnit.trim().toLowerCase();
        Activity activity = new Activity();
        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setValue(activityRequest.getValue());
        activity.setCustomUnit(customUnit);
        activity.setActivityDateTime(activityRequest.getActivityDateTime());
        activity.setHabit(habit);
        activity.setFocus(focus);
        activity.setId(null);
        activity.setUser(user);
        activity.setHabit(habit);
        activity.setFocus(focus);
        Activity savedActivity = activityRepository.save(activity);
        log.info("saved activity");
        updateGoalProgress(user.getId(), focus,savedActivity.getCustomUnit(),savedActivity.getActivityDateTime(), activity.getValue());
        updateHabitProgress(user.getId(), focus,customUnit,activityRequest.getActivityDateTime(), activityRequest.getValue());
        log.info("Activity added successfully for user: {}",authentication.getName());
        ActivityResponse response = modelMapper.map(savedActivity,ActivityResponse.class);
        response.setHabitTitle(habitTitle);
        response.setFocusName(focusName);
        return response;
    }

//    @Cacheable(value = "activities",key = "#authentication.name + '_' + #page + '_' + #size")
    public Page<ActivityResponse> findByUser(Authentication authentication, int page, int size) {
        log.info("CACHE MISS - Fetching from DataBase: {}", authentication.getName());
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activities = activityRepository.findByUserId(user.getId(), pageable);
        if(activities.isEmpty()){
            log.warn("Activity not present for user: {}",authentication.getName());
            throw new ActivityNotFoundException("Activity not found with Id : ",user.getId());
        }

        log.info("Activity returned successfully for user: {}",authentication.getName());

        return activities.map(activity -> modelMapper.map(activity, ActivityResponse.class));
    }

//    @CacheEvict(value = "activities", key = "#authentication.name + '_0_10'")
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


    //Activity cannot be updated in first phase so this method does not work properly
//    @CachePut(value = "activities", key = "#authentication.name + '_0_10'")
    public ActivityResponse updateActivity(Long id,ActivityUpdateRequestDTO updateRequestDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Activity activity = activityRepository.findById(id).orElseThrow(
                ()-> new ActivityNotFoundException("Activity not found with Id : ",id)
        );
        if(!activity.getUser().getId().equals(user.getId())){
            throw new AccessDeniedExceptionUser("Access Denied");
        }

        Focus focus = focusRepository.findById(updateRequestDTO.getFocusId()).orElse(null);
        if(focus!=null){
            activity.setFocus(focus);
        }

        Activity updated = activityRepository.save(activity);
//        updateGoalProgress(user.getId(), updated.getActivityCategory());
        return modelMapper.map(updated,ActivityResponse.class);
    }


//    @CacheEvict(value = "goals", allEntries = true)
    public void updateGoalProgress(Long userId, Focus focus, String customUnit, Instant activityDateTime,Double value){
        List<Goal> goals = goalRepository.findByUserIdAndFocusId(userId,focus.getId());

        LocalDate activityDate =
                activityDateTime.atZone(ZoneOffset.UTC).toLocalDate();

        for(Goal goal : goals){
            if(goal.getCustomUnit().equals(customUnit)
                    && !activityDate.isBefore(goal.getStartDate())
                    && !activityDate.isAfter(goal.getEndDate())){
                goal.setTotalProgress(goal.getTotalProgress()+value);

                if(goal.getTotalProgress()>=goal.getTargetValue()){
                    goal.setStatus(GoalStatus.DONE);
                }else{
                    goal.setStatus(GoalStatus.IN_PROGRESS);
                }
            }
        }

        goalRepository.saveAll(goals);
    }

    public void updateHabitProgress(
            Long userId,
            Focus focus,
            String customUnit,
            Instant activityDateTime,
            Double value) {

        List<Habit> habits =
                habitRepository.findByUserIdAndFocusIdAndCustomUnit(
                        userId, focus.getId(), customUnit);

        for (Habit habit : habits) {

            LocalDate activityDate = activityDateTime.atZone(ZoneOffset.UTC).toLocalDate();

            LocalDate startDate;
            LocalDate endDate;
            LocalDate prevStartDate;
            LocalDate prevEndDate;

            switch (habit.getFrequency()) {
                case DAILY -> {
                    startDate = activityDate;
                    endDate = startDate.plusDays(1);
                    prevStartDate = startDate.minusDays(1);
                    prevEndDate = startDate;
                }
                case WEEKLY -> {
                    startDate = activityDate.with(DayOfWeek.MONDAY);
                    endDate = startDate.plusWeeks(1);
                    prevStartDate = startDate.minusWeeks(1);
                    prevEndDate = startDate;
                }
                case MONTHLY -> {
                    startDate = activityDate.withDayOfMonth(1);
                    endDate = startDate.plusMonths(1);
                    prevStartDate = startDate.minusMonths(1);
                    prevEndDate = startDate;
                }
                default -> { continue; }
            }

            Instant start = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant end = endDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant prevStart = prevStartDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant prevEnd = prevEndDate.atStartOfDay(ZoneOffset.UTC).toInstant();

            List<Activity> activities =
                    activityRepository
                            .findByUserIdAndHabitAndActivityDateTimeGreaterThanEqualAndActivityDateTimeLessThan(
                                    userId, habit, start, end);

            double totalValue = activities.stream().mapToDouble(Activity::getValue).sum();

            if (totalValue < habit.getTargetValue()) {
                continue; // not completed, nothing to update
            }

            Instant lastCompletedAt = habit.getLastCompletedAt();

            // Was this exact period already completed by a prior activity?
            boolean alreadyCompletedThisPeriod =
                    lastCompletedAt != null
                            && !lastCompletedAt.isBefore(start)
                            && lastCompletedAt.isBefore(end);

            if (alreadyCompletedThisPeriod) {
                continue; // don't re-trigger streak/lastCompletedAt/status
            }

            // Was the immediately preceding period completed? -> continue streak, else reset
            boolean previousPeriodCompleted =
                    lastCompletedAt != null
                            && !lastCompletedAt.isBefore(prevStart)
                            && lastCompletedAt.isBefore(prevEnd);

            int currentStreak = habit.getCurrentStreak() == null ? 0 : habit.getCurrentStreak();
            currentStreak = previousPeriodCompleted ? currentStreak + 1 : 1;
            habit.setCurrentStreak(currentStreak);

            int longestStreak = habit.getLongestStreak() == null ? 0 : habit.getLongestStreak();
            habit.setLongestStreak(Math.max(longestStreak, currentStreak));

            // lastCompletedAt should be the timestamp of the activity that pushed total over target,
            // not just the current activity's timestamp — find the earliest-in-time activity at which
            // the cumulative sum first reached targetValue.
            habit.setLastCompletedAt(
                    findCompletionTimestamp(activities, habit.getTargetValue())
            );

            if (habit.getStatus() != HabitStatus.ACTIVE) {
                habit.setStatus(HabitStatus.ACTIVE);
            }

            habitRepository.save(habit);
        }
    }

    private Instant findCompletionTimestamp(List<Activity> activities, double targetValue) {
        List<Activity> sorted = activities.stream()
                .sorted(Comparator.comparing(Activity::getActivityDateTime))
                .toList();
        double running = 0;
        for (Activity a : sorted) {
            running += a.getValue();
            if (running >= targetValue) {
                return a.getActivityDateTime();
            }
        }
        // fallback, shouldn't happen since caller already checked total >= target
        return sorted.get(sorted.size() - 1).getActivityDateTime();
    }
}
