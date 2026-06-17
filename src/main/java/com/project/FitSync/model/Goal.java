package com.project.FitSync.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Goal {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        private String description;

        @Enumerated(EnumType.STRING)
        private ActivityType type;

        @Enumerated(EnumType.STRING)
        private GoalStatus status = GoalStatus.PENDING;

        private double targetValue;

        private double totalProgress;


        private LocalDate startDate;

        private LocalDate endDate;


        @ManyToOne
        private User user;
}
