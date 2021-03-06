package com.aliuken.jobvacanciesapp.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.aliuken.jobvacanciesapp.Constants;

public class DateUtils {

	private static final DateUtils SINGLETON_INSTANCE = new DateUtils();

	private DateUtils() {}

	public static DateUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	public static String convertToStringForWebPageField(LocalDate localDate) {
		final String text = DateUtils.convertToStringWithDefaultValue(localDate, Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD);
		return text;
	}

	public static String convertToStringForSerialization(LocalDate localDate) {
		final String text = DateUtils.convertToStringWithDefaultValue(localDate, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return text;
	}

	public static LocalDate convertFromStringForSerialization(String dateTimeString) {
		final LocalDate localDate = DateUtils.convertFromStringWithDefaultValue(dateTimeString, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
	    return localDate;
	}

	private static String convertToStringWithDefaultValue(LocalDate localDate, String defaultValue) {
		if(localDate == null) {
			return defaultValue;
		}

		final String text = localDate.format(Constants.DATE_FORMATTER);

		return text;
	}

	private static LocalDate convertFromStringWithDefaultValue(String dateString, String defaultValue) {
		if(dateString == null || dateString.equals(defaultValue)) {
			return null;
		}

		final LocalDate localDate = LocalDate.parse(dateString, Constants.DATE_FORMATTER);
	    return localDate;
	}

	public static String convertToString(LocalDate localDate) {
		if(localDate == null) {
			return null;
		}

		final String text = localDate.format(Constants.DATE_FORMATTER);
		return text;
	}

	public static LocalDate convertFromString(String dateString) {
		if(dateString == null) {
			return null;
		}

		final LocalDate localDate = LocalDate.parse(dateString, Constants.DATE_FORMATTER);
	    return localDate;
	}

	public static Date convertToDate(LocalDate localDate) {
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

	public static LocalDate convertFromDate(Date date) {
		if(date == null) {
			return null;
		}

		final Instant instant = date.toInstant();
		final ZoneId zoneId = ZoneId.systemDefault();
		final LocalDate localDate = LocalDate.ofInstant(instant, zoneId);

	    return localDate;
	}

	public static String convertFromDateToString(Date date) {
		if(date == null) {
			return null;
		}

		final LocalDate localDate = DateUtils.convertFromDate(date);
		final String text = DateUtils.convertToString(localDate);

		return text;
	}

	public static Date convertFromStringToDate(String dateTimeString) {
		if(dateTimeString == null) {
			return null;
		}

		final LocalDate localDate = DateUtils.convertFromString(dateTimeString);
		final Date date = DateUtils.convertToDate(localDate);

		return date;
	}

}