package com.aliuken.jobvacanciesapp.config;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.DateUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private JobVacanciesAppConfigPropertiesBean jobVacanciesAppConfigPropertiesBean;

	@Autowired
	private LocaleConfig localeConfig;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		final String jobCompanyLogosPath = jobVacanciesAppConfigPropertiesBean.getJobCompanyLogosPath();
		final String authUserCurriculumFilesPath = jobVacanciesAppConfigPropertiesBean.getAuthUserCurriculumFilesPath();
		final String resourceLocation1 = StringUtils.getStringJoined("file:", jobCompanyLogosPath);
		final String resourceLocation2 = StringUtils.getStringJoined("file:", authUserCurriculumFilesPath);
		registry.addResourceHandler("/job-company-logos/**").addResourceLocations(resourceLocation1);
		registry.addResourceHandler("/auth-user-curriculum-files/**").addResourceLocations(resourceLocation2);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		final LocaleChangeInterceptor localeChangeInterceptor = localeConfig.localeChangeInterceptor();
	    registry.addInterceptor(localeChangeInterceptor);
	}

	@Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, new Formatter<LocalDate>() {
			@Override
			public String print(LocalDate localDate, Locale locale) {
				String text = DateUtils.convertToString(localDate);
				return text;
			}

			@Override
			public LocalDate parse(String text, Locale locale) throws ParseException {
				LocalDate localDate = DateUtils.convertFromString(text);
				return localDate;
			}
        });

        registry.addFormatterForFieldType(LocalDateTime.class, new Formatter<LocalDateTime>() {
			@Override
			public String print(LocalDateTime localDateTime, Locale locale) {
				String text = DateTimeUtils.convertToString(localDateTime);
				return text;
			}

			@Override
			public LocalDateTime parse(String text, Locale locale) throws ParseException {
				LocalDateTime localDateTime = DateTimeUtils.convertFromString(text);
				return localDateTime;
			}
        });
    }

}
