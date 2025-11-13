package com.example.HireTrack.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "jobApplications")
@ToString(exclude = "jobApplications")
@Schema(description = "User entity representing a registered user in the system")
public class User {
    
    @Id
    @Column(unique = true, nullable = false)
    @Schema(description = "User's email address (unique identifier)", example = "john.doe@example.com", required = true)
    private String email;
    
    @Column(nullable = false)
    @Schema(description = "User's full name", example = "John Doe", required = true)
    private String name;
    
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Timestamp when the user was created (automatically set, cannot be changed)", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the user was last updated (automatically set on update)", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<JobApplication> jobApplications = new ArrayList<>();
    
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

