package com.aliuken.jobvacanciesapp.util.javase.time;

import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils implements TemporalUtils<LocalDateTime> {

	private static final DateTimeUtils SINGLETON_INSTANCE = new DateTimeUtils();
	private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	private DateTimeUtils(){}

	public static DateTimeUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public String getTemporalPattern() {
		return DATE_TIME_PATTERN;
	}

	@Override
	public DateTimeFormatter getTemporalFormatter() {
		return DATE_TIME_FORMATTER;
	}

	@Override
	public Class<LocalDateTime> getTemporalClass() {
		return LocalDateTime.class;
	}
}