package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserCredentialsDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;

public class AuthUserCredentialsConverter extends EntityToDtoConverter<AuthUserCredentials, AuthUserCredentialsDTO> {

	private static final AuthUserCredentialsConverter SINGLETON_INSTANCE = new AuthUserCredentialsConverter();

	private AuthUserCredentialsConverter() {
		super(AuthUserCredentialsConverter::conversionFunction, AuthUserCredentials.class, AuthUserCredentialsDTO.class, AuthUserCredentialsDTO[]::new);
	}

	public static AuthUserCredentialsConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static AuthUserCredentialsDTO conversionFunction(final AuthUserCredentials authUserCredentials) {
		final AuthUserCredentialsDTO authUserCredentialsDTO;
		if(authUserCredentials != null) {
			authUserCredentialsDTO = new AuthUserCredentialsDTO(
				authUserCredentials.getId(),
				authUserCredentials.getEmail(),
				null,
				null,
				null
			);
		} else {
			authUserCredentialsDTO = AuthUserCredentialsDTO.getNewInstance();
		}
		return authUserCredentialsDTO;
	}
}