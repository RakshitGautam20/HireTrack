package com.example.HireTrack.service;

import com.example.HireTrack.model.ApplicationStatus;
import com.example.HireTrack.model.JobApplication;
import com.example.HireTrack.model.User;
import com.example.HireTrack.repository.JobApplicationRepository;
import com.example.HireTrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    /**
     * Sends daily reminder emails to users with APPLIED applications.
     * Runs every day at 9:00 AM (server time).
     * Cron expression: second minute hour day month weekday
     * 0 0 9 * * * = Every day at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional(readOnly = true)
    public void sendDailyApplicationReminders() {
        log.info("Starting daily application reminder job...");
        
        try {
            // Find all distinct user emails who have APPLIED applications
            List<String> userEmails = jobApplicationRepository
                    .findDistinctUserEmailsByStatus(ApplicationStatus.APPLIED);
            
            if (userEmails.isEmpty()) {
                log.info("No users with APPLIED applications found. Skipping email reminders.");
                return;
            }
            
            log.info("Found {} users with APPLIED applications. Sending reminder emails...", userEmails.size());
            
            int successCount = 0;
            int failureCount = 0;
            
            for (String email : userEmails) {
                try {
                    // Get user details
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User not found: " + email));
                    
                    // Get all APPLIED applications for this user
                    List<JobApplication> appliedApplications = jobApplicationRepository
                            .findByUserEmailAndStatus(email, ApplicationStatus.APPLIED);
                    
                    if (!appliedApplications.isEmpty()) {
                        emailService.sendApplicationReminder(
                                user.getEmail(),
                                user.getName(),
                                appliedApplications
                        );
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("Failed to send reminder email to user: {}", email, e);
                    failureCount++;
                }
            }
            
            log.info("Daily reminder job completed. Success: {}, Failures: {}", successCount, failureCount);
            
        } catch (Exception e) {
            log.error("Error in daily application reminder job", e);
        }
    }
}

