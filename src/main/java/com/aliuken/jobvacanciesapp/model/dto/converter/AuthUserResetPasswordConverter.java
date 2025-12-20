package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserResetPasswordDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;

public class AuthUserResetPasswordConverter extends EntityToDtoConverter<AuthUserResetPassword, AuthUserResetPasswordDTO> {

	private static final AuthUserResetPasswordConverter SINGLETON_INSTANCE = new AuthUserResetPasswordConverter();

	private AuthUserResetPasswordConverter() {
		super(AuthUserResetPasswordConverter::conversionFunction, AuthUserResetPassword.class, AuthUserResetPasswordDTO.class, AuthUserResetPasswordDTO[]::new);
	}

	public static AuthUserResetPasswordConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static AuthUserResetPasswordDTO conversionFunction(final AuthUserResetPassword authUserResetPassword) {
		final AuthUserResetPasswordDTO authUserResetPasswordDTO;
		if(authUserResetPassword != null) {
			authUserResetPasswordDTO = new AuthUserResetPasswordDTO(
				authUserResetPassword.getId(),
				authUserResetPassword.getEmail(),
				authUserResetPassword.getUuid(),
				null,
				null
			);
		} else {
			authUserResetPasswordDTO = AuthUserResetPasswordDTO.getNewInstance();
		}
		return authUserResetPasswordDTO;
	}
}