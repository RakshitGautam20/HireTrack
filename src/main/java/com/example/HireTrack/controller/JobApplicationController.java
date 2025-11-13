package com.example.HireTrack.controller;

import com.example.HireTrack.model.ApplicationStatus;
import com.example.HireTrack.model.JobApplication;
import com.example.HireTrack.service.ExportService;
import com.example.HireTrack.service.JobApplicationService;
import com.example.HireTrack.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Job Applications", description = "API endpoints for managing job applications")
public class JobApplicationController {
    
    private final JobApplicationService service;
    private final ExportService exportService;
    private final UserService userService;
    
    // 1. Get all applications in database
    @Operation(
            summary = "Get all applications",
            description = "Retrieve a paginated list of all job applications in the database, regardless of user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all applications",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobApplication.class)))
    @GetMapping("/all")
    public ResponseEntity<Page<JobApplication>> getAllApplications(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllApplications(pageable));
    }
    
    // 2. Get all applications for a specific user
    @Operation(
            summary = "Get all applications for a user",
            description = "Retrieve all job applications for a specific user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user's applications",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobApplication.class)))
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<JobApplication>> getAllApplicationsByUser(
            @Parameter(description = "User's email address", required = true)
            @PathVariable String userEmail) {
        return ResponseEntity.ok(service.getAllApplicationsByUser(userEmail));
    }
    
    // 3. Get all applications with a particular status
    @Operation(
            summary = "Get all applications by status",
            description = "Retrieve all job applications with a specific status, regardless of user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applications",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobApplication.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplication>> getAllApplicationsByStatus(
            @Parameter(description = "Application status", required = true,
                    schema = @Schema(type = "string", allowableValues = {"APPLIED", "INTERVIEW", "REJECTED", "OFFERED", "HIRED"}))
            @PathVariable String status) {
        try {
            ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(service.getAllApplicationsByStatus(applicationStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 4. Get all applications of a user with a particular status
    @Operation(
            summary = "Get user applications by status",
            description = "Retrieve all job applications for a specific user with a particular status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applications",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobApplication.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @GetMapping("/user/{userEmail}/status/{status}")
    public ResponseEntity<List<JobApplication>> getUserApplicationsByStatus(
            @Parameter(description = "User's email address", required = true)
            @PathVariable String userEmail,
            @Parameter(description = "Application status", required = true,
                    schema = @Schema(type = "string", allowableValues = {"APPLIED", "INTERVIEW", "REJECTED", "OFFERED", "HIRED"}))
            @PathVariable String status) {
        try {
            ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(service.getUserApplicationsByStatus(userEmail, applicationStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 5. Create an application for a user
    @Operation(
            summary = "Create a new job application",
            description = "Create a new job application for a user. The application will be automatically linked to the user's email. Created and updated dates are automatically set."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobApplication.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or user not found")
    })
    @PostMapping
    public ResponseEntity<JobApplication> createApplication(
            @Parameter(description = "Job application details", required = true)
            @RequestBody JobApplication application,
            @Parameter(description = "User's email address", required = true)
            @RequestParam String userEmail) {
        try {
            JobApplication created = service.createApplication(application, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 6. Update status of a particular application of a user
    @Operation(
            summary = "Update application status",
            description = "Update the status of a specific job application. Only the user who owns the application can update it. Updated date is automatically set to current time, created date remains unchanged."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobApplication.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplication> updateApplicationStatus(
            @Parameter(description = "Application UUID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "New status", required = true,
                    schema = @Schema(type = "string", allowableValues = {"APPLIED", "INTERVIEW", "REJECTED", "OFFERED", "HIRED"}))
            @RequestParam String status,
            @Parameter(description = "User's email address", required = true)
            @RequestParam String userEmail) {
        try {
            ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
            JobApplication updated = service.updateApplicationStatus(id, applicationStatus, userEmail);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 7. Delete an application
    @Operation(
            summary = "Delete a job application",
            description = "Delete a job application. Only the user who owns the application can delete it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Application deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @Parameter(description = "Application UUID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "User's email address", required = true)
            @RequestParam String userEmail) {
        try {
            service.deleteApplication(id, userEmail);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 8. Export user applications as CSV
    @Operation(
            summary = "Export user applications as CSV",
            description = "Export all job applications for a specific user as a CSV file"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV file generated successfully",
                    content = @Content(mediaType = "text/csv")),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userEmail}/export/csv")
    public ResponseEntity<byte[]> exportUserApplicationsAsCSV(
            @Parameter(description = "User's email address", required = true)
            @PathVariable String userEmail) {
        try {
            var user = userService.getUserByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
            
            List<JobApplication> applications = service.getAllApplicationsByUser(userEmail);
            byte[] csvData = exportService.exportToCSV(user, applications);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", 
                    "job_applications_" + userEmail.replace("@", "_") + ".csv");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

