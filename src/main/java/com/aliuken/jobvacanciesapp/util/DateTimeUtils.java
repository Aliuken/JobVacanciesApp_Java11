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
		if(localDateTime == null) {
			return defaultValue;
		}

		final String text = localDateTime.format(Constants.DATE_TIME_FORMATTER);
		return text;
	}

	private static LocalDateTime convertFromStringWithDefaultValue(String dateTimeString, String defaultValue) {
		if(dateTimeString == null || dateTimeString.equals(defaultValue)) {
			return null;
		}

		final LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, Constants.DATE_TIME_FORMATTER);

	    return localDateTime;
	}

	public static String convertToString(LocalDateTime localDateTime) {
		if(localDateTime == null) {
			return null;
		}

		final String text = localDateTime.format(Constants.DATE_TIME_FORMATTER);
		return text;
	}

	public static LocalDateTime convertFromString(String dateTimeString) {
		if(dateTimeString == null) {
			return null;
		}

		final LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, Constants.DATE_TIME_FORMATTER);
	    return localDateTime;
	}

	public static Date convertToDate(LocalDateTime localDateTime) {
		if(localDateTime == null) {
			return null;
		}

		final ZoneId zoneId = ZoneId.systemDefault();
		final ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		final Instant instant = zonedDateTime.toInstant();
		final Date date = Date.from(instant);

	    return date;
	}

	public static LocalDateTime convertFromDate(Date date) {
		if(date == null) {
			return null;
		}

		final Instant instant = date.toInstant();
		final ZoneId zoneId = ZoneId.systemDefault();
		final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

	    return localDateTime;
	}

	public static String convertFromDateToString(Date date) {
		if(date == null) {
			return null;
		}

		final LocalDateTime localDateTime = DateTimeUtils.convertFromDate(date);
		final String text = DateTimeUtils.convertToString(localDateTime);

		return text;
	}

	public static Date convertFromStringToDate(String dateTimeString) {
		if(dateTimeString == null) {
			return null;
		}

		final LocalDateTime localDateTime = DateTimeUtils.convertFromString(dateTimeString);
		final Date date = DateTimeUtils.convertToDate(localDateTime);

		return date;
	}

}