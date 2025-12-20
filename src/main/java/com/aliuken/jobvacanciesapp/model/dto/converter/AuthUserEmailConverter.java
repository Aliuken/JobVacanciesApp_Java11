package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserEmailDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;

public class AuthUserEmailConverter extends EntityToDtoConverter<AuthUserCredentials, AuthUserEmailDTO> {

	private static final AuthUserEmailConverter SINGLETON_INSTANCE = new AuthUserEmailConverter();

	private AuthUserEmailConverter() {
		super(AuthUserEmailConverter::conversionFunction, AuthUserCredentials.class, AuthUserEmailDTO.class, AuthUserEmailDTO[]::new);
	}

	public static AuthUserEmailConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static AuthUserEmailDTO conversionFunction(final AuthUserCredentials authUserCredentials) {
		final AuthUserEmailDTO authUserEmailDTO;
		if(authUserCredentials != null) {
			authUserEmailDTO = new AuthUserEmailDTO(
				authUserCredentials.getId(),
				authUserCredentials.getEmail()
			);
		} else {
			authUserEmailDTO = AuthUserEmailDTO.getNewInstance();
		}
		return authUserEmailDTO;
	}
}