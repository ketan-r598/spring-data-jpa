package io.java_core.taskmanagementapi.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(CorsConfigurationProperties.class)
public class CorsConfiguration {

    private final CorsConfigurationProperties corsConfigurationProperties;

    public CorsConfiguration(CorsConfigurationProperties corsConfigurationProperties) {
        this.corsConfigurationProperties = corsConfigurationProperties;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(corsConfigurationProperties.allowedOrigins().toArray(new String[0]))
                        .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

}
