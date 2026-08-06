package io.java_core.taskmanagementapi.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TaskProperties.class)
public class TaskConfiguration {

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper()
//
//                // Serialization
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .enable(SerializationFeature.INDENT_OUTPUT)
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//                // Desrialization
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
//                ;
//    }
}
