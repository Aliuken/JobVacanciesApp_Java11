package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;

@Converter(autoApply=true)
public class LanguageConverter implements AttributeConverter<Language, String> {

	@Override
	public String convertToDatabaseColumn(final Language language) {
		if(language == null) {
			return null;
		}

		final String languageCode = language.getCode();
		return languageCode;
	}

	@Override
	public Language convertToEntityAttribute(final String languageCode) {
		final Language language = Language.findByCode(languageCode);
		return language;
	}
}