package com.aliuken.jobvacanciesapp.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.aliuken.jobvacanciesapp.Constants;

public class DateTimeUtils {

	private static final DateTimeUtils SINGLETON_INSTANCE = new DateTimeUtils();

	private DateTimeUtils() {}

	public static DateTimeUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	public static String convertToStringForWebPageField(LocalDateTime localDateTime) {
		final String text = DateTimeUtils.convertToStringWithDefaultValue(localDateTime, Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD);
		return text;
	}

	public static String convertToStringForSerialization(LocalDateTime localDateTime) {
		final String text = DateTimeUtils.convertToStringWithDefaultValue(localDateTime, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return text;
	}

	public static LocalDateTime convertFromStringForSerialization(String dateTimeString) {
		final LocalDateTime localDateTime = DateTimeUtils.convertFromStringWithDefaultValue(dateTimeString, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
	    return localDateTime;
	}

	private static String convertToStringWithDefaultValue(LocalDateTime localDateTime, String defaultValue) {
		final String text;
		if(localDateTime != null) {
			text = localDateTime.format(Constants.DATE_TIME_FORMATTER);
		} else {
			text = defaultValue;
		}

		return text;
	}

	private static LocalDateTime convertFromStringWithDefaultValue(String dateTimeString, String defaultValue) {
		final LocalDateTime localDateTime;
		if(dateTimeString != null && !dateTimeString.equals(defaultValue)) {
			localDateTime = LocalDateTime.parse(dateTimeString, Constants.DATE_TIME_FORMATTER);
		} else {
			localDateTime = null;
		}

	    return localDateTime;
	}

	public static String convertToString(LocalDateTime localDateTime) {
		final String text;
		if(localDateTime != null) {
			text = localDateTime.format(Constants.DATE_TIME_FORMATTER);
		} else {
			text = null;
		}

		return text;
	}

	public static LocalDateTime convertFromString(String dateTimeString) {
		final LocalDateTime localDateTime;
		if(dateTimeString != null) {
			localDateTime = LocalDateTime.parse(dateTimeString, Constants.DATE_TIME_FORMATTER);
		} else {
			localDateTime = null;
		}

	    return localDateTime;
	}

	public static Date convertToDate(LocalDateTime localDateTime) {
		final Date date;
		if(localDateTime != null) {
			final ZoneId zoneId = ZoneId.systemDefault();
			final ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
			final Instant instant = zonedDateTime.toInstant();
			date = Date.from(instant);
		} else {
			date = null;
		}

	    return date;
	}

	public static LocalDateTime convertFromDate(Date date) {
		final LocalDateTime localDateTime;
		if(date != null) {
			final Instant instant = date.toInstant();
			final ZoneId zoneId = ZoneId.systemDefault();
			localDateTime = LocalDateTime.ofInstant(instant, zoneId);
		} else {
			localDateTime = null;
		}

	    return localDateTime;
	}

	public static String convertFromDateToString(Date date) {
		final String text;
		if(date != null) {
			final LocalDateTime localDateTime = DateTimeUtils.convertFromDate(date);
			text = DateTimeUtils.convertToString(localDateTime);
		} else {
			text = null;
		}

		return text;
	}

	public static Date convertFromStringToDate(String dateTimeString) {
		final Date date;
		if(dateTimeString != null) {
			final LocalDateTime localDateTime = DateTimeUtils.convertFromString(dateTimeString);
			date = DateTimeUtils.convertToDate(localDateTime);
		} else {
			date = null;
		}

		return date;
	}

}