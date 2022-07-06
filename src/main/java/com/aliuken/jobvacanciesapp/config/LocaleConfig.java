package com.aliuken.jobvacanciesapp.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class LocaleConfig {

	@Bean
	public SessionLocaleResolver localeResolver() {
		final SessionLocaleResolver localeResolver = new SessionLocaleResolver() {
			@Override
			public Locale resolveLocale(HttpServletRequest httpServletRequest) {
				String language = httpServletRequest.getParameter("lang");

				Locale locale;
				if (language != null && !language.isEmpty()) {
					locale = StringUtils.parseLocaleString(language);
				} else {
					locale = Locale.US;
				}

				return locale;
			}
		};

		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public MessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setCacheSeconds(60); // reload messages every 60 seconds
		return messageSource;
	}

}