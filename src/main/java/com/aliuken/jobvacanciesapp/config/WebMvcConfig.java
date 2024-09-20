package com.aliuken.jobvacanciesapp.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.aliuken.jobvacanciesapp.model.formatter.LocalDateFormatter;
import com.aliuken.jobvacanciesapp.model.formatter.LocalDateTimeFormatter;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private I18nConfig i18nConfig;

	@Autowired
	private LocalDateFormatter localDateFormatter;

	@Autowired
	private LocalDateTimeFormatter localDateTimeFormatter;

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry resourceHandlerRegistry) {
		final String authUserCurriculumFilesPath = configPropertiesBean.getAuthUserCurriculumFilesPath();
		final String authUserEntityQueryFilesPath = configPropertiesBean.getAuthUserEntityQueryFilesPath();
		final String jobCompanyLogosPath = configPropertiesBean.getJobCompanyLogosPath();
		final String resourceLocation1 = StringUtils.getStringJoined("file:", authUserCurriculumFilesPath);
		final String resourceLocation2 = StringUtils.getStringJoined("file:", authUserEntityQueryFilesPath);
		final String resourceLocation3 = StringUtils.getStringJoined("file:", jobCompanyLogosPath);
		resourceHandlerRegistry.addResourceHandler("/auth-user-curriculum-files/**").addResourceLocations(resourceLocation1);
		resourceHandlerRegistry.addResourceHandler("/auth-user-entity-query-files/**").addResourceLocations(resourceLocation2);
		resourceHandlerRegistry.addResourceHandler("/job-company-logos/**").addResourceLocations(resourceLocation3);
	}

	@Override
	public void addInterceptors(final InterceptorRegistry interceptorRegistry) {
		final LocaleChangeInterceptor localeChangeInterceptor = i18nConfig.localeChangeInterceptor();
		interceptorRegistry.addInterceptor(localeChangeInterceptor);
	}

	@Override
	public void addFormatters(final FormatterRegistry formatterRegistry) {
		formatterRegistry.addFormatterForFieldType(LocalDate.class, localDateFormatter);
		formatterRegistry.addFormatterForFieldType(LocalDateTime.class, localDateTimeFormatter);
	}
}
