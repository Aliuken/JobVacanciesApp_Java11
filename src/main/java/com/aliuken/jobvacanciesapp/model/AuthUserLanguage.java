package com.aliuken.jobvacanciesapp.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Locale;

import javax.validation.constraints.NotNull;

public enum AuthUserLanguage implements Serializable {
	ENGLISH("en", "English", new Locale("en")),
	SPANISH("es", "Español", new Locale("es"));

	@NotNull
	private final String code;

	@NotNull
	private final String messageValue;

	@NotNull
	private final Locale locale;

	private AuthUserLanguage(@NotNull final String code, @NotNull final String messageValue, @NotNull final Locale locale) {
		this.code = code;
		this.messageValue = messageValue;
		this.locale = locale;
	}

	public String getCode() {
		return code;
	}

	public String getMessageValue() {
		return messageValue;
	}

	public Locale getLocale() {
		return locale;
	}

	public static AuthUserLanguage findByCode(final String code) {
		if(code == null || code.isEmpty()) {
			return null;
		}

		final AuthUserLanguage authUserLanguage = EnumSet.allOf(AuthUserLanguage.class).stream().parallel()
				.filter(value -> value.code.equals(code))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("AuthUserLanguage code does not exist"));

		return authUserLanguage;
	}

}