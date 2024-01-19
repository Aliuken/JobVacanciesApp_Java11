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
	private LocaleConfig localeConfig;

	@Autowired
	private LocalDateFormatter localDateFormatter;

	@Autowired
	private LocalDateTimeFormatter localDateTimeFormatter;

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry resourceHandlerRegistry) {
		final String jobCompanyLogosPath = configPropertiesBean.getJobCompanyLogosPath();
		final String authUserCurriculumFilesPath = configPropertiesBean.getAuthUserCurriculumFilesPath();
		final String resourceLocation1 = StringUtils.getStringJoined("file:", jobCompanyLogosPath);
		final String resourceLocation2 = StringUtils.getStringJoined("file:", authUserCurriculumFilesPath);
		resourceHandlerRegistry.addResourceHandler("/job-company-logos/**").addResourceLocations(resourceLocation1);
		resourceHandlerRegistry.addResourceHandler("/auth-user-curriculum-files/**").addResourceLocations(resourceLocation2);
	}

	@Override
	public void addInterceptors(final InterceptorRegistry interceptorRegistry) {
		final LocaleChangeInterceptor localeChangeInterceptor = localeConfig.localeChangeInterceptor();
		interceptorRegistry.addInterceptor(localeChangeInterceptor);
	}

	@Override
	public void addFormatters(final FormatterRegistry formatterRegistry) {
		formatterRegistry.addFormatterForFieldType(LocalDate.class, localDateFormatter);
		formatterRegistry.addFormatterForFieldType(LocalDateTime.class, localDateTimeFormatter);
	}

}
