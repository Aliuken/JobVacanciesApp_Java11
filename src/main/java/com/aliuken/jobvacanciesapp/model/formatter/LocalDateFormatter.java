package com.aliuken.jobvacanciesapp.model.formatter;

import com.aliuken.jobvacanciesapp.Constants;
import org.jspecify.annotations.NonNull;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {
	@Override
	public @NonNull String print(final @NonNull LocalDate localDate, final @NonNull Locale locale) {
		final String text = Constants.DATE_UTILS.convertToString(localDate);
		return text;
	}

	@Override
	public @NonNull LocalDate parse(final @NonNull String text, final @NonNull Locale locale) throws ParseException {
		final LocalDate localDate = Constants.DATE_UTILS.convertFromString(text);
		return localDate;
	}
}