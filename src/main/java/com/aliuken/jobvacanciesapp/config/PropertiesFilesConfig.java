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
            new ClassPathResource("applicationWindows.properties"),
            new ClassPathResource("application.properties")
        };

//        final Resource[] resourceLinuxArray = new Resource[] {
//                new ClassPathResource("applicationLinux.properties"),
//                new ClassPathResource("application.properties")
//        };

        final PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        propertySourcesPlaceholderConfigurer.setLocations(resourceWindowsArray);
        return propertySourcesPlaceholderConfigurer;
    }
}