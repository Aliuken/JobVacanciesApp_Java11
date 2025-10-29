package com.aliuken.jobvacanciesapp.model.formatter;

import com.aliuken.jobvacanciesapp.Constants;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {
	@Override
	public String print(final LocalDate localDate, final Locale locale) {
		final String text = Constants.DATE_UTILS.convertToString(localDate);
		return text;
	}

	@Override
	public LocalDate parse(final String text, final Locale locale) throws ParseException {
		final LocalDate localDate = Constants.DATE_UTILS.convertFromString(text);
		return localDate;
	}
}