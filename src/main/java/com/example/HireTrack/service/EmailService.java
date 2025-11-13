package com.example.HireTrack.service;

import com.example.HireTrack.model.JobApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendApplicationReminder(String userEmail, String userName, List<JobApplication> applications) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail);
            message.setSubject("Reminder: You have pending job applications");
            message.setText(buildEmailContent(userName, applications));
            
            mailSender.send(message);
            log.info("Reminder email sent successfully to: {}", userEmail);
        } catch (Exception e) {
            log.error("Failed to send reminder email to: {}", userEmail, e);
        }
    }

    private String buildEmailContent(String userName, List<JobApplication> applications) {
        StringBuilder content = new StringBuilder();
        content.append("Hello ").append(userName).append(",\n\n");
        content.append("This is a friendly reminder that you have ").append(applications.size())
               .append(" job application(s) with status 'APPLIED' that may need your attention:\n\n");

        for (int i = 0; i < applications.size(); i++) {
            JobApplication app = applications.get(i);
            content.append(i + 1).append(". ")
                   .append(app.getPosition()).append(" at ").append(app.getCompany());
            
            if (app.getAppliedDate() != null) {
                content.append(" (Applied on: ").append(app.getAppliedDate()).append(")");
            }
            
            if (app.getNotes() != null && !app.getNotes().isEmpty()) {
                content.append("\n   Notes: ").append(app.getNotes());
            }
            
            content.append("\n\n");
        }

        content.append("Don't forget to follow up on these applications!\n\n");
        content.append("Best regards,\n");
        content.append("HireTrack Team");

        return content.toString();
    }
}

