package com.aliuken.jobvacanciesapp.util.javase.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;

public class DateTimeUtils implements TemporalUtils<LocalDateTime> {

	private static final DateTimeUtils SINGLETON_INSTANCE = new DateTimeUtils();
	private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	private DateTimeUtils(){}

	public static DateTimeUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public String convertToStringForWebPageField(final LocalDateTime localDateTime) {
		final String text = this.convertToStringWithDefaultValue(localDateTime, Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD);
		return text;
	}

	@Override
	public String convertToStringForSerialization(final LocalDateTime localDateTime) {
		final String text = this.convertToStringWithDefaultValue(localDateTime, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return text;
	}

	@Override
	public LocalDateTime convertFromStringForSerialization(final String dateTimeString) {
		final LocalDateTime localDateTime = this.convertFromStringWithDefaultValue(dateTimeString, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return localDateTime;
	}

	private String convertToStringWithDefaultValue(final LocalDateTime localDateTime, final String defaultValue) {
		if(localDateTime == null) {
			return defaultValue;
		}

		final String text = localDateTime.format(DATE_TIME_FORMATTER);
		return text;
	}

	private LocalDateTime convertFromStringWithDefaultValue(final String dateTimeString, final String defaultValue) {
		if(dateTimeString == null || dateTimeString.equals(defaultValue)) {
			return null;
		}

		final LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
		return localDateTime;
	}

	@Override
	public String convertToString(final LocalDateTime localDateTime) {
		if(localDateTime == null) {
			return null;
		}

		final String text = localDateTime.format(DATE_TIME_FORMATTER);
		return text;
	}

	@Override
	public LocalDateTime convertFromString(final String dateTimeString) {
		if(dateTimeString == null) {
			return null;
		}

		final LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
		return localDateTime;
	}

	@Override
	public Date convertToDate(final LocalDateTime localDateTime) {
		if(localDateTime == null) {
			return null;
		}

		final ZoneId zoneId = ZoneId.systemDefault();
		final ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		final Instant instant = zonedDateTime.toInstant();
		final Date date = Date.from(instant);

		return date;
	}

	@Override
	public LocalDateTime convertFromDate(final Date date) {
		if(date == null) {
			return null;
		}

		final Instant instant = date.toInstant();
		final ZoneId zoneId = ZoneId.systemDefault();
		final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

		return localDateTime;
	}

	@Override
	public String convertFromDateToString(final Date date) {
		if(date == null) {
			return null;
		}

		final LocalDateTime localDateTime = this.convertFromDate(date);
		final String text = this.convertToString(localDateTime);

		return text;
	}

	@Override
	public Date convertFromStringToDate(final String dateTimeString) {
		if(dateTimeString == null) {
			return null;
		}

		final LocalDateTime localDateTime = this.convertFromString(dateTimeString);
		final Date date = this.convertToDate(localDateTime);

		return date;
	}
}