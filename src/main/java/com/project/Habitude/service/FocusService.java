package com.project.Habitude.service;


import com.project.Habitude.dto.FocusRequestDTO;
import com.project.Habitude.dto.FocusResponseDTO;
import com.project.Habitude.dto.UpdateFocusRequestDTO;
import com.project.Habitude.model.Focus;
import com.project.Habitude.model.User;
import com.project.Habitude.repository.FocusRepository;
import com.project.Habitude.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FocusService {

    private final FocusRepository focusRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public FocusResponseDTO addFocus(FocusRequestDTO focusRequestDTO, Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Focus focus = new Focus();
        String name  = focusRequestDTO.getName().toLowerCase();
        focus.setName(name);
        focus.setUser(user);
        Focus  saved = focusRepository.save(focus);
        return modelMapper.map(saved,FocusResponseDTO.class);
    }

    public List<FocusResponseDTO> findFocus(Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        List<Focus> focusList = focusRepository.findByUserId(user.getId());

        return focusList.stream().map(focus -> modelMapper.map(focus,FocusResponseDTO.class)).toList();

    }

    @Transactional
    public void delete(Authentication authentication, String name) {
        String email  = authentication.getName();
        User user = userRepository.findByEmail(email);
        Focus focus = focusRepository.findByName(name);
        if(focus==null) throw new RuntimeException("No focus present with name");
        if (!focus.getUser().equals(user)) {
            throw new RuntimeException("You cannot delete this focus");
        }
        focusRepository.deleteByName(name);
    }

    public FocusResponseDTO update(UpdateFocusRequestDTO requestDTO, Authentication authentication) {
        String email  = authentication.getName();
        User user = userRepository.findByEmail(email);
        Focus focus = focusRepository.findByName(requestDTO.getName());
        if(focus==null) throw new RuntimeException("No focus present with name");
        String name = requestDTO.getName().toLowerCase();
        focus.setName(name);
        Focus saved = focusRepository.save(focus);
        return modelMapper.map(saved,FocusResponseDTO.class);

    }
}
