package com.project.Habitude.repository;

import com.project.Habitude.model.Focus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FocusRepository extends JpaRepository<Focus,Long> {
    List<Focus> findByUserId(Long userId);

    Focus findByName(String name);

    void deleteByName(String name);
}
