package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.Assert;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;

@Configuration
public class I18nConfig {

	@Bean
	@NonNull SessionLocaleResolver localeResolver() {
		final SessionLocaleResolver localeResolver = new SessionLocaleResolver() {
			@Override
			public @NonNull Locale resolveLocale(@NonNull HttpServletRequest httpServletRequest) {
				final String languageCode = httpServletRequest.getParameter("languageParam");
				final Locale locale = I18nUtils.getLocale(languageCode);
				return locale;
			}
		};

		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	@Bean
	@NonNull LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("languageParam");
		return localeChangeInterceptor;
	}

	@Bean
	@NonNull MessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setCacheSeconds(60); // reload messages every 60 seconds
		return messageSource;
	}

	@Bean
	@NonNull LocalValidatorFactoryBean localValidatorFactoryBean() {
		final MessageSource messageSource = messageSource();

		final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}
}