package com.project.FitSync.service;

import com.project.FitSync.dto.GoalRequestDTO;
import com.project.FitSync.dto.GoalResponseDTO;
import com.project.FitSync.dto.GoalStatusRequest;
import com.project.FitSync.dto.UpdateGoalRequest;
import com.project.FitSync.exceptions.AccessDeniedExceptionUser;
import com.project.FitSync.exceptions.GoalNotFoundException;
import com.project.FitSync.exceptions.UserNotFoundException;
import com.project.FitSync.model.Goal;
import com.project.FitSync.model.User;
import com.project.FitSync.repository.GoalRepository;
import com.project.FitSync.repository.UserRepository;
import com.project.FitSync.security.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final GoalRepository goalRepository;


    public GoalResponseDTO createGoal(Authentication authentication, GoalRequestDTO requestDTO) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Goal goal = modelMapper.map(requestDTO, Goal.class);
        goal.setId(null);
        goal.setUser(user);
        Goal savedGoal = goalRepository.save(goal);
        return modelMapper.map(savedGoal,GoalResponseDTO.class);
    }

    public List<GoalResponseDTO> getAllGoals(@Valid Authentication authentication)  {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();



        List<Goal> goals = goalRepository.findByUserId(user.getId());
        if(goals.isEmpty()){
            throw new GoalNotFoundException("User do not have any goals");
        }

        return goals.stream()
                .map(goal -> modelMapper.map(goal, GoalResponseDTO.class))
                .toList();

    }

    public void delete(@Valid Authentication authentication, Long id) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Goal goal = goalRepository.findById(id).orElseThrow(
                () -> new GoalNotFoundException("Goal not found with Id : " + id)
        );

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedExceptionUser("Access Denied");
        }

        goalRepository.deleteById(id);
    }

    public GoalResponseDTO update(Long id, UpdateGoalRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Goal goal = goalRepository.findById(id).orElseThrow(
                ()-> new GoalNotFoundException("Goal not found with Id : "+id)
        );
        if(!goal.getUser().getId().equals(user.getId())){
            throw new AccessDeniedExceptionUser("Access Denied");
        }
        System.out.println(request.getTitle());
        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setType(request.getType());
        goal.setTargetValue(request.getTargetValue());
        goal.setStartDate(request.getStartDate());
        goal.setEndDate(request.getEndDate());
        System.out.println(request.getDescription());
        Goal updated = goalRepository.save(goal);
        return modelMapper.map(updated,GoalResponseDTO.class);
    }


    public GoalResponseDTO updateStatus(@Valid Long id, GoalStatusRequest statusRequest, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        Goal goal = goalRepository.findById(id).orElseThrow(
                ()-> new GoalNotFoundException("Goal not found with Id : ",id)
        );
        if(!goal.getUser().getId().equals(user.getId())){
            throw new AccessDeniedExceptionUser("Access Denied");
        }
        goal.setStatus(statusRequest.getStatus());

        Goal updated = goalRepository.save(goal);
        return modelMapper.map(updated,GoalResponseDTO.class);
    }
}
