package com.example.HireTrack.service;

import com.example.HireTrack.model.JobApplication;
import com.example.HireTrack.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Export user's job applications as CSV
     */
    public byte[] exportToCSV(User user, List<JobApplication> applications) {
        StringBuilder csv = new StringBuilder();
        
        // CSV Header
        csv.append("ID,Company,Position,Status,Applied Date,Notes,Created At,Updated At\n");
        
        // CSV Data
        for (JobApplication app : applications) {
            csv.append(escapeCSV(app.getId().toString())).append(",")
                .append(escapeCSV(app.getCompany())).append(",")
                .append(escapeCSV(app.getPosition())).append(",")
                .append(escapeCSV(app.getStatus().toString())).append(",")
                .append(app.getAppliedDate() != null ? escapeCSV(app.getAppliedDate().format(DATE_FORMATTER)) : "").append(",")
                .append(escapeCSV(app.getNotes() != null ? app.getNotes() : "")).append(",")
                .append(app.getCreatedAt() != null ? escapeCSV(app.getCreatedAt().format(DATETIME_FORMATTER)) : "").append(",")
                .append(app.getUpdatedAt() != null ? escapeCSV(app.getUpdatedAt().format(DATETIME_FORMATTER)) : "")
                .append("\n");
        }
        
        return csv.toString().getBytes();
    }

    /**
     * Escape CSV special characters
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // If value contains comma, quote, or newline, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

