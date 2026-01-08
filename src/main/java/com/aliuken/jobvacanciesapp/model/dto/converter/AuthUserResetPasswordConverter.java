package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserResetPasswordDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
import org.jspecify.annotations.NonNull;

public class AuthUserResetPasswordConverter extends EntityToDtoConverter<AuthUserResetPassword, AuthUserResetPasswordDTO> {

	private static final @NonNull AuthUserResetPasswordConverter SINGLETON_INSTANCE = new AuthUserResetPasswordConverter();

	private AuthUserResetPasswordConverter() {
		super(AuthUserResetPassword.class, AuthUserResetPasswordDTO.class, AuthUserResetPasswordDTO[]::new);
	}

	public static @NonNull AuthUserResetPasswordConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull AuthUserResetPasswordDTO convert(final @NonNull AuthUserResetPassword authUserResetPassword) {
		final AuthUserResetPasswordDTO authUserResetPasswordDTO = new AuthUserResetPasswordDTO(
			authUserResetPassword.getId(),
			authUserResetPassword.getEmail(),
			authUserResetPassword.getUuid(),
			null,
			null
		);
		return authUserResetPasswordDTO;
	}
}