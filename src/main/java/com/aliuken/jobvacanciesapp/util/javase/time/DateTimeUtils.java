package com.aliuken.jobvacanciesapp.util.javase.time;

import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils implements TemporalUtils<LocalDateTime> {

	private static final @NonNull DateTimeUtils SINGLETON_INSTANCE = new DateTimeUtils();
	private static final @NonNull String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	private static final @NonNull DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	private DateTimeUtils(){}

	public static @NonNull DateTimeUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public @NonNull String getTemporalPattern() {
		return DATE_TIME_PATTERN;
	}

	@Override
	public @NonNull DateTimeFormatter getTemporalFormatter() {
		return DATE_TIME_FORMATTER;
	}

	@Override
	public @NonNull Class<LocalDateTime> getTemporalClass() {
		return LocalDateTime.class;
	}
}