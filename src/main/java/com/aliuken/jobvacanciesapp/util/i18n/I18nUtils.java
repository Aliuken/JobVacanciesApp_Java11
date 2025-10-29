package com.aliuken.jobvacanciesapp.util.i18n;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class I18nUtils {

	private I18nUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static String getInternationalizedMessageWithFieldNamePropertyParameter(final Language language, final String messageName, Object[]  messageParameters) {
		if(LogicalUtils.isNullOrEmpty(messageParameters)) {
			final String result = I18nUtils.getInternationalizedMessage(language, messageName, null);
			return result;
		}

		final String strippedFieldNameProperty = (String) messageParameters[0];
		final String fieldName = I18nUtils.getInternationalizedMessage(language, strippedFieldNameProperty, null);

		final String finalFieldName;
		if(fieldName == null) {
			finalFieldName = strippedFieldNameProperty;
		} else {
			final String strippedFieldName = fieldName.strip();
			if (strippedFieldName.isEmpty()) {
				finalFieldName = strippedFieldNameProperty;
			} else {
				finalFieldName = strippedFieldName;
			}
		}

		messageParameters[0] = finalFieldName;

		final String result = I18nUtils.getInternationalizedMessage(language, messageName, messageParameters);
		return result;
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