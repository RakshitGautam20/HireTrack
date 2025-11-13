package com.example.HireTrack.service;

import com.example.HireTrack.model.ApplicationStatus;
import com.example.HireTrack.model.JobApplication;
import com.example.HireTrack.model.User;
import com.example.HireTrack.repository.JobApplicationRepository;
import com.example.HireTrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    
    private final JobApplicationRepository repository;
    private final UserRepository userRepository;
    
    // 1. Get all applications in database
    public Page<JobApplication> getAllApplications(Pageable pageable) {
        return repository.findAllByOrderByAppliedDateDesc(pageable);
    }
    
    // 2. Get all applications for a specific user
    public List<JobApplication> getAllApplicationsByUser(String userEmail) {
        return repository.findByUserEmailOrderByAppliedDateDesc(userEmail);
    }
    
    // 3. Get all applications with a particular status
    public List<JobApplication> getAllApplicationsByStatus(ApplicationStatus status) {
        return repository.findByStatusOrderByAppliedDateDesc(status);
    }
    
    // 4. Get all applications of a user with a particular status
    public List<JobApplication> getUserApplicationsByStatus(String userEmail, ApplicationStatus status) {
        return repository.findByUserEmailAndStatusOrderByAppliedDateDesc(userEmail, status);
    }
    
    // 5. Create an application for a user
    @Transactional
    public JobApplication createApplication(JobApplication application, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        application.setUser(user);
        return repository.save(application);
    }
    
    // 6. Update status of a particular application of a user
    @Transactional
    public JobApplication updateApplicationStatus(UUID id, ApplicationStatus status, String userEmail) {
        JobApplication application = repository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Job application not found with id: " + id + " for user: " + userEmail));
        application.setStatus(status);
        return repository.save(application);
    }
    
    // 7. Delete an application
    @Transactional
    public void deleteApplication(UUID id, String userEmail) {
        if (!repository.existsByIdAndUserEmail(id, userEmail)) {
            throw new RuntimeException("Job application not found with id: " + id + " for user: " + userEmail);
        }
        repository.deleteById(id);
    }
}

