package com.example.users.nexa.service;

import com.example.users.nexa.dto.UserDTO;
import com.example.users.nexa.entity.UserEntity;
import com.example.users.nexa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Convert Entity -> DTO
    private UserDTO convertToDTO(UserEntity entity) {
        return new UserDTO(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDob(),
                entity.getGender(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAnnualSalary(),
                entity.getCreditScore()
        );
    }

    // Convert DTO -> Entity
    private UserEntity convertToEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUserId(dto.getUserId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDob(dto.getDob());
        entity.setGender(dto.getGender());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAnnualSalary(dto.getAnnualSalary());
        entity.setCreditScore(dto.getCreditScore());
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        return entity;
    }

    // Get all users
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public Optional<UserDTO> getUserById(UUID id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    // Create user
    public UserDTO createUser(UserDTO dto) {
        UserEntity entity = convertToEntity(dto);
        entity.setUserId(null); // Ensure new UUID is generated
        UserEntity saved = userRepository.save(entity);
        return convertToDTO(saved);
    }

    // Update user
    public Optional<UserDTO> updateUser(UUID id, UserDTO dto) {
        return userRepository.findById(id).map(existing -> {
            existing.setFirstName(dto.getFirstName());
            existing.setLastName(dto.getLastName());
            existing.setDob(dto.getDob());
            existing.setGender(dto.getGender());
            existing.setEmail(dto.getEmail());
            existing.setPhone(dto.getPhone());
            existing.setAnnualSalary(dto.getAnnualSalary());
            existing.setCreditScore(dto.getCreditScore());
            UserEntity updated = userRepository.save(existing);
            return convertToDTO(updated);
        });
    }

    // Delete user
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
