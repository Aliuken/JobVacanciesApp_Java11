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

		return authUserLanguage.getCode();
	}

	@Override
	public AuthUserLanguage convertToEntityAttribute(String code) {
		AuthUserLanguage authUserLanguage = AuthUserLanguage.findByCode(code);

		return authUserLanguage;
	}

}