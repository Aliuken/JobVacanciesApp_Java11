package com.aliuken.jobvacanciesapp.util.javase.time;

import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils implements TemporalUtils<LocalDate> {

	private static final @NonNull DateUtils SINGLETON_INSTANCE = new DateUtils();
	private static final @NonNull String DATE_PATTERN = "dd-MM-yyyy";
	private static final @NonNull DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	private DateUtils(){}

	public static @NonNull DateUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public @NonNull String getTemporalPattern() {
		return DATE_PATTERN;
	}

	@Override
	public @NonNull DateTimeFormatter getTemporalFormatter() {
		return DATE_FORMATTER;
	}

	@Override
	public @NonNull Class<LocalDate> getTemporalClass() {
		return LocalDate.class;
	}
}