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
		final String text;
		if(localDate != null) {
			text = localDate.format(Constants.DATE_FORMATTER);
		} else {
			text = defaultValue;
		}

		return text;
	}
	
	private static LocalDate convertFromStringWithDefaultValue(String dateString, String defaultValue) {
		final LocalDate localDate;
		if(dateString != null && !dateString.equals(defaultValue)) {
			localDate = LocalDate.parse(dateString, Constants.DATE_FORMATTER);
		} else {
			localDate = null;
		}

	    return localDate;
	}

	public static String convertToString(LocalDate localDate) {
		final String text;
		if(localDate != null) {
			text = localDate.format(Constants.DATE_FORMATTER);
		} else {
			text = null;
		}

		return text;
	}

	public static LocalDate convertFromString(String dateString) {
		final LocalDate localDate;
		if(dateString != null) {
			localDate = LocalDate.parse(dateString, Constants.DATE_FORMATTER);
		} else {
			localDate = null;
		}

	    return localDate;
	}

	public static Date convertToDate(LocalDate localDate) {
		final Date date;
		if(localDate != null) {
			final ZoneId zoneId = ZoneId.systemDefault();
			final LocalDateTime localDateTime = localDate.atStartOfDay();
			final ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
			final Instant instant = zonedDateTime.toInstant();
			date = Date.from(instant);
		} else {
			date = null;
		}

	    return date;
	}

	public static LocalDate convertFromDate(Date date) {
		final LocalDate localDate;
		if(date != null) {
			final Instant instant = date.toInstant();
			final ZoneId zoneId = ZoneId.systemDefault();
			localDate = LocalDate.ofInstant(instant, zoneId);
		} else {
			localDate = null;
		}

	    return localDate;
	}

	public static String convertFromDateToString(Date date) {
		final String text;
		if(date != null) {
			final LocalDate localDate = DateUtils.convertFromDate(date);
			text = DateUtils.convertToString(localDate);
		} else {
			text = null;
		}

		return text;
	}

	public static Date convertFromStringToDate(String dateTimeString) {
		final Date date;
		if(dateTimeString != null) {
			final LocalDate localDate = DateUtils.convertFromString(dateTimeString);
			date = DateUtils.convertToDate(localDate);
		} else {
			date = null;
		}

		return date;
	}

}