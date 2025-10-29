package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Configuration
public class I18nConfig {

	@Bean
	SessionLocaleResolver localeResolver() {
		final SessionLocaleResolver localeResolver = new SessionLocaleResolver() {
			@Override
			public Locale resolveLocale(HttpServletRequest httpServletRequest) {
				final String languageCode = httpServletRequest.getParameter("languageParam");
				if(LogicalUtils.isNullOrEmptyString(languageCode)) {
					return Locale.US;
				}

				final Locale locale = org.springframework.util.StringUtils.parseLocaleString(languageCode);
				return locale;
			}
		};

		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("languageParam");
		return localeChangeInterceptor;
	}

	@Bean
	MessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setCacheSeconds(60); // reload messages every 60 seconds
		return messageSource;
	}

	@Bean
	LocalValidatorFactoryBean localValidatorFactoryBean() {
		final MessageSource messageSource = messageSource();

		final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}
}