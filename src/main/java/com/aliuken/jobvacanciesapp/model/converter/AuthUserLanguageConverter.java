package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;

@Converter(autoApply=true)
public class AuthUserLanguageConverter implements AttributeConverter<AuthUserLanguage, String> {

	@Override
	public String convertToDatabaseColumn(AuthUserLanguage authUserLanguage) {
		if (authUserLanguage == null) {
			return null;
		}

		final String authUserLanguageCode = authUserLanguage.getCode();
		return authUserLanguageCode;
	}

	@Override
	public AuthUserLanguage convertToEntityAttribute(String code) {
		final AuthUserLanguage authUserLanguage = AuthUserLanguage.findByCode(code);
		return authUserLanguage;
	}

}