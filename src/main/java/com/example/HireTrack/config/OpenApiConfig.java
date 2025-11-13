package com.example.HireTrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${SERVER_URL:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI hireTrackOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription(serverUrl.contains("localhost") ? "Local development server" : "Production server");
        
        Info info = new Info()
                .title("HireTrack API")
                .version("1.0.0")
                .description("A RESTful API for tracking job applications. " +
                        "Users can manage their job applications with features like status tracking, " +
                        "search, and filtering. Each user is identified by their email address.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("hiretrack-api")
                .pathsToMatch("/api/**")
                .build();
    }
}

