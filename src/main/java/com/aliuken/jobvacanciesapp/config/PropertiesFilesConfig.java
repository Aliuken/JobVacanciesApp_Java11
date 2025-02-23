package com.aliuken.jobvacanciesapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class PropertiesFilesConfig {
    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final Resource[] resourceWindowsArray = new Resource[] {
            new ClassPathResource("application.properties"),
            new ClassPathResource("applicationWindows.properties")
        };

//        final Resource[] resourceLinuxArray = new Resource[] {
//                new ClassPathResource("application.properties"),
//                new ClassPathResource("applicationLinux.properties")
//        };

        final PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        propertySourcesPlaceholderConfigurer.setLocations(resourceWindowsArray);
        return propertySourcesPlaceholderConfigurer;
    }
}