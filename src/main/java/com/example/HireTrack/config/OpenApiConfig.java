package com.example.HireTrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hireTrackOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local development server");

//        Contact contact = new Contact();
//        contact.setEmail("contact@hiretrack.com");
//        contact.setName("HireTrack API Support");

//        License license = new License()
//                .name("MIT License")
//                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("HireTrack API")
                .version("1.0.0")
                .description("A RESTful API for tracking job applications. " +
                        "Users can manage their job applications with features like status tracking, " +
                        "search, and filtering. Each user is identified by their email address.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("hiretrack-api")
                .pathsToMatch("/api/**")
                .build();
    }
}

