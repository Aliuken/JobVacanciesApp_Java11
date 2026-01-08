package com.aliuken.jobvacanciesapp.model.formatter;

import com.aliuken.jobvacanciesapp.Constants;
import org.jspecify.annotations.NonNull;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
	@Override
	public @NonNull String print(final @NonNull LocalDateTime localDateTime, final @NonNull Locale locale) {
		final String text = Constants.DATE_TIME_UTILS.convertToString(localDateTime);
		return text;
	}

	@Override
	public @NonNull LocalDateTime parse(final @NonNull String text, final @NonNull Locale locale) throws ParseException {
		final LocalDateTime localDateTime = Constants.DATE_TIME_UTILS.convertFromString(text);
		return localDateTime;
	}
}