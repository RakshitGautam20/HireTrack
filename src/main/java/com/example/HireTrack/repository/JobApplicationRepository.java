package com.example.HireTrack.repository;

import com.example.HireTrack.model.ApplicationStatus;
import com.example.HireTrack.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    
    // Get all applications (no filter) - with pagination
    Page<JobApplication> findAllByOrderByAppliedDateDesc(Pageable pageable);
    
    // Get all applications (no filter) - without pagination (for backward compatibility)
    List<JobApplication> findAllByOrderByAppliedDateDesc();
    
    // Get all applications by status (no user filter)
    List<JobApplication> findByStatusOrderByAppliedDateDesc(ApplicationStatus status);
    
    // Get all applications for a specific user
    List<JobApplication> findByUserEmailOrderByAppliedDateDesc(String userEmail);
    
    // Get all applications of a user with a particular status
    List<JobApplication> findByUserEmailAndStatusOrderByAppliedDateDesc(String userEmail, ApplicationStatus status);
    
    // Find by ID and user email (for security)
    Optional<JobApplication> findByIdAndUserEmail(UUID id, String userEmail);
    
    boolean existsByIdAndUserEmail(UUID id, String userEmail);
    
    // Find all applications with APPLIED status for a specific user
    List<JobApplication> findByUserEmailAndStatus(String userEmail, ApplicationStatus status);
    
    // Find distinct user emails who have APPLIED applications
    @Query("SELECT DISTINCT u.email FROM JobApplication j JOIN j.user u WHERE j.status = :status")
    List<String> findDistinctUserEmailsByStatus(@Param("status") ApplicationStatus status);
}

