package com.aliuken.jobvacanciesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.aliuken.jobvacanciesapp.config.JobVacanciesAppConfigPropertiesBean;

@SpringBootApplication
@EnableConfigurationProperties(JobVacanciesAppConfigPropertiesBean.class)
@ConfigurationPropertiesScan("com.aliuken.jobvacanciesapp.config")
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}