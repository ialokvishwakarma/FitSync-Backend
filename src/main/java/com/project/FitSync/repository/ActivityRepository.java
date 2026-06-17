package com.project.FitSync.repository;

import com.project.FitSync.model.Activity;
import com.project.FitSync.model.ActivityType;
import com.project.FitSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    //    Optional<Activity> findByActivityId(Long activityId);
    //    Activity findByActivityId(Long activityId);

    List<Activity> findByUser(User user);

    List<Activity> findByUserId(Long id);

    @Query("SELECT SUM(a.duration) from Activity a WHERE a.user.id = :userId AND a.type = :type")
    Double sumDurationByUserIdAndType(@Param("userId") Long id, @Param("type") ActivityType type);

    @Query("SELECT SUM(a.distance) FROM Activity a WHERE a.user.id =:userId AND a.type = :type")
    Double sumDistanceByUserIdAndType(@Param("userId") Long id, @Param("type") ActivityType type);

    @Query("SELECT SUM(a.caloriesBurned) FROM Activity a WHERE a.user.id =:userId AND a.type = :type")
    Double sumCaloriesByUserIdAndType(@Param("userId") Long id, @Param("type") ActivityType type);
}
