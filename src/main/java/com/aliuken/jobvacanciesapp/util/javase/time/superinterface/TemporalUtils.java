package com.aliuken.jobvacanciesapp.util.javase.time.superinterface;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

public interface TemporalUtils<T extends Temporal> {

	public abstract String getTemporalPattern();

	public abstract DateTimeFormatter getTemporalFormatter();

	public abstract String convertToStringForWebPageField(final T temporal);

	public abstract String convertToStringForSerialization(final T temporal);

	public abstract T convertFromStringForSerialization(final String temporalString);

	public abstract String convertToString(final T temporal);

	public abstract T convertFromString(final String temporalString);

	public abstract Date convertToDate(final T temporal);

	public abstract T convertFromDate(final Date date);

	public abstract String convertFromDateToString(final Date date);

	public abstract Date convertFromStringToDate(final String temporalString);

}
