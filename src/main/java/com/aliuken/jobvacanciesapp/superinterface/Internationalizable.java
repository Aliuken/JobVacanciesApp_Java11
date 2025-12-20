package com.aliuken.jobvacanciesapp.superinterface;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;

public interface Internationalizable {
	public abstract String getMessageName();

	public default String getMessage(final String languageCode) {
		final String messageName = this.getMessageName();
		final String message = I18nUtils.getInternationalizedMessage(languageCode, messageName, null);
		return message;
	}

	public default String getMessage(final Language language) {
		final String messageName = this.getMessageName();
		final String message = I18nUtils.getInternationalizedMessage(language, messageName, null);
		return message;
	}

	public default String getMessage(final String languageCode, Object[] messageParameters) {
		final String messageName = this.getMessageName();
		final String message = I18nUtils.getInternationalizedMessage(languageCode, messageName, messageParameters);
		return message;
	}

	public default String getMessage(final Language language, Object[] messageParameters) {
		final String messageName = this.getMessageName();
		final String message = I18nUtils.getInternationalizedMessage(language, messageName, messageParameters);
		return message;
	}
}
