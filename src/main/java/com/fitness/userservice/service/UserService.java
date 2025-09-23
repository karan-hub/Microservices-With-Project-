package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.exception.AllReadyExistsException;
import com.fitness.userservice.exception.UserNotFoundException;
import com.fitness.userservice.models.User;
import com.fitness.userservice.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw  new AllReadyExistsException("Email Already  Registered ");

        User  user =  User.builder()
                .role(request.getRole())
                .email(request.getEmail())
                .password(request.getPassword())
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .build();

        User savedUser =userRepository.save(user);
        return  buildReposition(savedUser);
    }

    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("User Not Found For Buddy " + userId)
        );
        return buildReposition(user);
    }

    private UserResponse buildReposition(User savedUser) {
        UserResponse response = UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .role(savedUser.getRole())
                .build();
        return  response;
    }


    public Boolean existByUserId(String userId) {

        return  userRepository.existsById(userId);
    }
}
