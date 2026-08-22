package com.project.Habitude.repository;

import com.project.Habitude.model.Activity;
import com.project.Habitude.model.ActivityCategory;
import com.project.Habitude.model.Habit;
import com.project.Habitude.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    //    Optional<Activity> findByActivityId(Long activityId);
    //    Activity findByActivityId(Long activityId);

    List<Activity> findByUser(User user);

    List<Activity> findByUserId(Long id);
    Page<Activity> findByUserId(Long id, Pageable pageable);

    List<Activity> findByUserIdAndHabit(Long userId, Habit habit);

    List<Activity> findByUserIdAndHabitAndActivityDateTimeGreaterThanEqualAndActivityDateTimeLessThan(Long userId, Habit habit, Instant start, Instant end);

//    Double sumTotalValueBetweenStartAndEndDate();


}
