package com.aliuken.jobvacanciesapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix="jobvacanciesapp")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class JobVacanciesAppConfigPropertiesBean {

	private final String jobCompanyLogosPath;
	private final String authUserCurriculumFilesPath;
	private final boolean anonymousAccessAllowed;
	private final boolean useAjaxToRefreshJobCompanyLogos;

}