package com.example.HireTrack.controller;

import com.example.HireTrack.model.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Users", description = "API endpoints for managing users")
public class UserController {
    
    private final UserService service;
    
    @Operation(
            summary = "Get all users",
            description = "Retrieve a paginated list of all registered users"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllUsers(pageable));
    }
    
    @Operation(
            summary = "Get user by email",
            description = "Retrieve a specific user by their email address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "User's email address", required = true)
            @PathVariable String email) {
        return service.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(
            summary = "Create a new user",
            description = "Register a new user with email and name. Email must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "User details", required = true)
            @RequestBody User user) {
        try {
            User created = service.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @Operation(
            summary = "Update user information",
            description = "Update an existing user's information (e.g., name). Email cannot be changed. Updated date is automatically set to current time, created date remains unchanged."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User's email address", required = true)
            @PathVariable String email,
            @Parameter(description = "Updated user details", required = true)
            @RequestBody User user) {
        try {
            User updated = service.updateUser(email, user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
            summary = "Delete a user",
            description = "Delete a user and all their associated job applications"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User's email address", required = true)
            @PathVariable String email) {
        try {
            service.deleteUser(email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

