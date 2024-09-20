package com.aliuken.jobvacanciesapp.util.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.springcore.di.BeanFactoryUtils;

public class I18nUtils {

	private I18nUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", I18nUtils.class.getName()));
	}

	public static String getInternationalizedMessage(final String languageCode, final String messageName, final Object[] messageParameters) {
		final Language language = Language.findByCode(languageCode);
		final String internationalizedMessage = I18nUtils.getInternationalizedMessage(language, messageName, messageParameters);

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
}