package com.aliuken.jobvacanciesapp.util.javase.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;

public class DateUtils implements TemporalUtils<LocalDate> {

	private static final DateUtils SINGLETON_INSTANCE = new DateUtils();

	private DateUtils() {}

	public static DateUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public String convertToStringForWebPageField(final LocalDate localDate) {
		final String text = this.convertToStringWithDefaultValue(localDate, Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD);
		return text;
	}

	@Override
	public String convertToStringForSerialization(final LocalDate localDate) {
		final String text = this.convertToStringWithDefaultValue(localDate, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return text;
	}

	@Override
	public LocalDate convertFromStringForSerialization(final String dateString) {
		final LocalDate localDate = this.convertFromStringWithDefaultValue(dateString, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return localDate;
	}

	private String convertToStringWithDefaultValue(final LocalDate localDate, final String defaultValue) {
		if(localDate == null) {
			return defaultValue;
		}

		final String text = localDate.format(Constants.DATE_FORMATTER);
		return text;
	}

	private LocalDate convertFromStringWithDefaultValue(final String dateString, final String defaultValue) {
		if(dateString == null || dateString.equals(defaultValue)) {
			return null;
		}

		final LocalDate localDate = LocalDate.parse(dateString, Constants.DATE_FORMATTER);
		return localDate;
	}

	@Override
	public String convertToString(final LocalDate localDate) {
		if(localDate == null) {
			return null;
		}

		final String text = localDate.format(Constants.DATE_FORMATTER);
		return text;
	}

	@Override
	public LocalDate convertFromString(final String dateString) {
		if(dateString == null) {
			return null;
		}

		final LocalDate localDate = LocalDate.parse(dateString, Constants.DATE_FORMATTER);
		return localDate;
	}

	@Override
	public Date convertToDate(final LocalDate localDate) {
		if(localDate == null) {
			return null;
		}

		final ZoneId zoneId = ZoneId.systemDefault();
		final LocalDateTime localDateTime = localDate.atStartOfDay();
		final ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		final Instant instant = zonedDateTime.toInstant();
		final Date date = Date.from(instant);

		return date;
	}

	@Override
	public LocalDate convertFromDate(final Date date) {
		if(date == null) {
			return null;
		}

		final Instant instant = date.toInstant();
		final ZoneId zoneId = ZoneId.systemDefault();
		final LocalDate localDate = LocalDate.ofInstant(instant, zoneId);

		return localDate;
	}

	@Override
	public String convertFromDateToString(final Date date) {
		if(date == null) {
			return null;
		}

		final LocalDate localDate = this.convertFromDate(date);
		final String text = this.convertToString(localDate);

		return text;
	}

	@Override
	public Date convertFromStringToDate(final String dateString) {
		if(dateString == null) {
			return null;
		}

		final LocalDate localDate = this.convertFromString(dateString);
		final Date date = this.convertToDate(localDate);

		return date;
	}

}