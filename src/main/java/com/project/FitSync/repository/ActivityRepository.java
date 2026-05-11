package com.project.FitSync.repository;

import com.project.FitSync.model.Activity;
import com.project.FitSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUser(User user);

    List<Activity> findByUserId(Long id);
//    Optional<Activity> findByActivityId(Long activityId);

//    Activity findByActivityId(Long activityId);
}
