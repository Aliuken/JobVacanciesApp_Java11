package com.aliuken.jobvacanciesapp.model.formatter;

import com.aliuken.jobvacanciesapp.Constants;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
	@Override
	public String print(final LocalDateTime localDateTime, final Locale locale) {
		final String text = Constants.DATE_TIME_UTILS.convertToString(localDateTime);
		return text;
	}

	@Override
	public LocalDateTime parse(final String text, final Locale locale) throws ParseException {
		final LocalDateTime localDateTime = Constants.DATE_TIME_UTILS.convertFromString(text);
		return localDateTime;
	}
}