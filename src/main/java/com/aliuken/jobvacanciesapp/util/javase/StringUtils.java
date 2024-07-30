package com.aliuken.jobvacanciesapp.util.javase;

import java.util.Collection;
import java.util.Locale;
import java.util.StringJoiner;

import org.springframework.context.MessageSource;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.RandomCharactersEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.springcore.di.BeanFactoryUtils;

public class StringUtils {

	private StringUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", StringUtils.class.getName()));
	}

	public static String getStringJoined(final CharSequence... elements) {
		final CharSequence delimiter = Constants.EMPTY_STRING;

		final StringJoiner stringJoiner = new StringJoiner(delimiter);
		if(elements != null) {
			for(final CharSequence element : elements) {
				stringJoiner.add(element);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static String getStringJoinedWithDelimiters(CharSequence delimiter, CharSequence prefix, CharSequence suffix, final Collection<String> elements) {
		if(delimiter == null) {
			delimiter = Constants.EMPTY_STRING;
		}

		if(prefix == null) {
			prefix = Constants.EMPTY_STRING;
		}

		if(suffix == null) {
			suffix = Constants.EMPTY_STRING;
		}

		final StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);
		if(elements != null) {
			for(final CharSequence element : elements) {
				stringJoiner.add(element);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static String getInternationalizedMessage(final String languageCode, final String messageName, final Object[] messageParameters) {
		final Language language = Language.findByCode(languageCode);
		final String internationalizedMessage = StringUtils.getInternationalizedMessage(language, messageName, messageParameters);

		return internationalizedMessage;
	}

	public static String getInternationalizedMessage(Language language, final String messageName, final Object[] messageParameters) {
		if(language == null) {
			language = Language.ENGLISH;
		}

		final Locale locale = language.getLocale();
		final MessageSource messageSource = BeanFactoryUtils.getBean(MessageSource.class);
		final String message = messageSource.getMessage(messageName, messageParameters, locale);

		return message;
	}

	public static String getRandomString(RandomCharactersEnum randomCharactersEnum, int size) {
		final String result;
		if(randomCharactersEnum != null && randomCharactersEnum.getCharacters() != null && size > 0) {
			final String characters = randomCharactersEnum.getCharacters();
			final int charactersSize = randomCharactersEnum.getCharactersSize();

			final StringBuilder stringBuilder = new StringBuilder();
			do {
				final double randomDouble = Math.random();
				final double randomPositionDouble = randomDouble * charactersSize;
				final int randomPosition = (int) randomPositionDouble;
				final char character = characters.charAt(randomPosition);
				stringBuilder.append(character);
				size--;
			} while(size > 0);
			result = stringBuilder.toString();
		} else {
			result = Constants.EMPTY_STRING;
		}
		return result;
	}
}