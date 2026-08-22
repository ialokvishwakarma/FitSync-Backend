package com.project.Habitude.repository;

import com.project.Habitude.model.ActivityCategory;
import com.project.Habitude.model.Goal;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserId(@Valid Long id);

    List<Goal> findByUserIdAndFocusId(Long userId,Long focusId);

//    List<Goal> findByUserIdAndActivityCategoryAndCustomUnit(Long id, ActivityCategory activityCategory,String customUnit);
}
