package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;

public enum Language implements Serializable {
	ENGLISH("en", "English", new Locale("en")),
	SPANISH("es", "Español", new Locale("es"));

	@NotNull
	private final String code;

	@NotNull
	private final String messageValue;

	@NotNull
	private final Locale locale;

	private Language(@NotNull final String code, @NotNull final String messageValue, @NotNull final Locale locale) {
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

	public static Language findByCode(final String code) {
		if(LogicalUtils.isNullOrEmpty(code)) {
			return null;
		}

		final Language language = Constants.PARALLEL_STREAM_UTILS.ofEnum(Language.class)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Language code does not exist"));

		return language;
	}

}