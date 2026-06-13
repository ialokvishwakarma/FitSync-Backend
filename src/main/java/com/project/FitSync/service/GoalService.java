package com.project.FitSync.service;

import com.project.FitSync.dto.GoalRequestDTO;
import com.project.FitSync.dto.GoalResponseDTO;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.model.Goal;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.GoalRepository;
import com.project.FitSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final GoalRepository goalRepository;


    public GoalResponseDTO createGoal( GoalRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserID()).orElseThrow(
                ()-> new UserNotFoundException("User not found with Id : ",requestDTO.getUserID()));
        System.out.println(user);
        Goal goal = modelMapper.map(requestDTO, Goal.class);
        goal.setId(null);
        goal.setUser(user);
        Goal savedGoal = goalRepository.save(goal);
        return modelMapper.map(savedGoal,GoalResponseDTO.class);
    }
}
