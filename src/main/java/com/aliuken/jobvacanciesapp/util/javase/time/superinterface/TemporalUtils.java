package com.aliuken.jobvacanciesapp.util.javase.time.superinterface;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

public interface TemporalUtils<T extends Temporal> {

	public abstract @NonNull Class<T> getTemporalClass();

	public abstract @NonNull String getTemporalPattern();

	public abstract @NonNull DateTimeFormatter getTemporalFormatter();

	public default @NonNull String convertToStringForWebPageField(final T temporal) {
		final String text = this.convertToStringWithDefaultValue(temporal, Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD);
		return text;
	}

	public default @NonNull String convertToStringForSerialization(final T temporal) {
		final String text = this.convertToStringWithDefaultValue(temporal, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return text;
	}

	public default T convertFromStringForSerialization(final String dateString) {
		final T temporal = this.convertFromStringWithDefaultValue(dateString, Constants.DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING);
		return temporal;
	}

	public default String convertToString(final T temporal) {
		if(temporal == null) {
			return null;
		}

		final String text = this.convertToStringWithNonNullTemporal(temporal);
		return text;
	}

	public default T convertFromString(final String dateString) {
		if(dateString == null) {
			return null;
		}

		final T temporal = this.convertFromStringWithNonDefaultDateString(dateString);
		return temporal;
	}

	public default Date convertToDate(final T temporal) {
		if(temporal == null) {
			return null;
		}

		final LocalDateTime localDateTime;
		if(temporal instanceof LocalDate) {
			final LocalDate localDate = (LocalDate) temporal;
			localDateTime = localDate.atStartOfDay();
		} else {
			localDateTime = (LocalDateTime) temporal;
		}

		final ZoneId zoneId = ZoneId.systemDefault();
		final ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		final Instant instant = zonedDateTime.toInstant();
		final Date date = Date.from(instant);

		return date;
	}

	public default T convertFromDate(final Date date) {
		if(date == null) {
			return null;
		}

		final Instant instant = date.toInstant();
		final ZoneId zoneId = ZoneId.systemDefault();

		final Class<T> temporalClass = this.getTemporalClass();

		final T temporal;
		if(LocalDate.class.equals(temporalClass)) {
			final LocalDate localDate = LocalDate.ofInstant(instant, zoneId);
			temporal = GenericsUtils.cast(localDate);
		} else {
			final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
			temporal = GenericsUtils.cast(localDateTime);
		}

		return temporal;
	}

	public default String convertFromDateToString(final Date date) {
		if(date == null) {
			return null;
		}

		final T temporal = this.convertFromDate(date);
		final String text = this.convertToString(temporal);

		return text;
	}

	public default Date convertFromStringToDate(final String dateString) {
		if(dateString == null) {
			return null;
		}

		final T temporal = this.convertFromString(dateString);
		final Date date = this.convertToDate(temporal);

		return date;
	}

	private @NonNull String convertToStringWithDefaultValue(final T temporal, final @NonNull String defaultValue) {
		if(temporal == null) {
			return defaultValue;
		}

		final String text = this.convertToStringWithNonNullTemporal(temporal);
		return text;
	}

	private @NonNull String convertToStringWithNonNullTemporal(final @NonNull T temporal) {
		final DateTimeFormatter temporalFormatter = this.getTemporalFormatter();

		final String text;
		if(temporal instanceof LocalDate) {
			final LocalDate localDate = (LocalDate) temporal;
			text = localDate.format(temporalFormatter);
		} else {
			final LocalDateTime localDateTime = (LocalDateTime) temporal;
			text = localDateTime.format(temporalFormatter);
		}
		return text;
	}

	private T convertFromStringWithDefaultValue(final String dateString, final @NonNull String defaultValue) {
		if(dateString == null || dateString.equals(defaultValue)) {
			return null;
		}

		final T temporal = this.convertFromStringWithNonDefaultDateString(dateString);
		return temporal;
	}

	private @NonNull T convertFromStringWithNonDefaultDateString(final @NonNull String dateString) {
		final Class<T> temporalClass = this.getTemporalClass();
		final DateTimeFormatter temporalFormatter = this.getTemporalFormatter();

		final T temporal;
		if(LocalDate.class.equals(temporalClass)) {
			final LocalDate localDate = LocalDate.parse(dateString, temporalFormatter);
			temporal = GenericsUtils.cast(localDate);
		} else {
			final LocalDateTime localDateTime = LocalDateTime.parse(dateString, temporalFormatter);
			temporal = GenericsUtils.cast(localDateTime);
		}

		return temporal;
	}
}
