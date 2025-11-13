package com.example.HireTrack.service;

import com.example.HireTrack.model.User;
import com.example.HireTrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository repository;
    
    public Page<User> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
    
    public boolean userExists(String email) {
        return repository.existsByEmail(email);
    }
    
    @Transactional
    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return repository.save(user);
    }
    
    @Transactional
    public User updateUser(String email, User userDetails) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        user.setName(userDetails.getName());
        
        return repository.save(user);
    }
    
    @Transactional
    public void deleteUser(String email) {
        if (!repository.existsByEmail(email)) {
            throw new RuntimeException("User not found with email: " + email);
        }
        repository.deleteById(email);
    }
}

