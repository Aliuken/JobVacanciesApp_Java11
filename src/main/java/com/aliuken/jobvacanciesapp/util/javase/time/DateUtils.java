package com.aliuken.jobvacanciesapp.util.javase.time;

import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils implements TemporalUtils<LocalDate> {

	private static final DateUtils SINGLETON_INSTANCE = new DateUtils();
	private static final String DATE_PATTERN = "dd-MM-yyyy";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	private DateUtils(){}

	public static DateUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public String getTemporalPattern() {
		return DATE_PATTERN;
	}

	@Override
	public DateTimeFormatter getTemporalFormatter() {
		return DATE_FORMATTER;
	}

	@Override
	public Class<LocalDate> getTemporalClass() {
		return LocalDate.class;
	}
}