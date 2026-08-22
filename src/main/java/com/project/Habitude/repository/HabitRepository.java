package com.project.Habitude.repository;

import com.project.Habitude.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {
    List<Habit> findByUserId(Long userId);

    List<Habit> findByUserIdAndFocusId(Long userId, Long id);

    List<Habit> findByUserIdAndFocusIdAndCustomUnit(Long userId, Long id, String customUnit);
}
