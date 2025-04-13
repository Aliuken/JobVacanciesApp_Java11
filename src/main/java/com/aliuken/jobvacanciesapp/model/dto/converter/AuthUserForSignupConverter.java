package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserForSignupDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;

public class AuthUserForSignupConverter extends EntityToDtoConverter<AuthUser, AuthUserForSignupDTO> {

	private static final AuthUserForSignupConverter SINGLETON_INSTANCE = new AuthUserForSignupConverter();

	private AuthUserForSignupConverter() {
		super(AuthUserForSignupConverter::conversionFunction, AuthUser.class, AuthUserForSignupDTO.class, AuthUserForSignupDTO[]::new);
	}

	public static AuthUserForSignupConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static AuthUserForSignupDTO conversionFunction(final AuthUser authUser) {
		final AuthUserForSignupDTO authUserForSignupDTO;
		if(authUser != null) {
			authUserForSignupDTO = new AuthUserForSignupDTO(
				authUser.getEmail(),
				null,
				null,
				authUser.getName(),
				authUser.getSurnames(),
				authUser.getLanguage().getCode()
			);
		} else {
			authUserForSignupDTO = AuthUserForSignupDTO.getNewInstance();
		}
		return authUserForSignupDTO;
	}
}