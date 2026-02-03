package com.recursion.portfolioManager.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allowing all origins, modify as per your needs.
        registry.addMapping("/**") // Apply CORS configuration to all endpoints.
                .allowedOrigins("http://localhost:5173", "https://your-frontend-domain.com") // Add allowed frontend origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allowed headers
                .allowCredentials(true) // Allow credentials like cookies
                .maxAge(3600); // Caching pre-flight requests for 1 hour
    }
}

