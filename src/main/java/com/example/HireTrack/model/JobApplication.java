package com.example.HireTrack.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
@Schema(description = "Job application entity representing a job application submitted by a user")
public class JobApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @Schema(description = "Unique identifier (UUID) for the job application - auto-generated, read-only", example = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    
    @Column(name = "company", nullable = false)
    @Schema(description = "Name of the company", example = "Google", required = true)
    private String company;
    
    @Column(name = "position", nullable = false)
    @Schema(description = "Job position title", example = "Software Engineer", required = true)
    private String position;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Schema(description = "Current status of the application", 
            example = "APPLIED", 
            required = true,
            allowableValues = {"APPLIED", "INTERVIEW", "REJECTED", "OFFERED", "HIRED"})
    private ApplicationStatus status;
    
    @Column(name = "applied_date")
    @Schema(description = "Date when the application was submitted", example = "2024-01-15")
    private LocalDate appliedDate;
    
    @Column(name = "notes", length = 2000)
    @Schema(description = "Additional notes about the application", example = "Applied through referral")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    @JsonIgnore
    @Schema(hidden = true)
    private User user;
    
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Timestamp when the application was created (automatically set, cannot be changed)", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the application was last updated (automatically set on update)", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

