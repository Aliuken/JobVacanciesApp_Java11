package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserForSignupDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import org.jspecify.annotations.NonNull;

public class AuthUserForSignupConverter extends EntityToDtoConverter<AuthUser, AuthUserForSignupDTO> {

	private static final @NonNull AuthUserForSignupConverter SINGLETON_INSTANCE = new AuthUserForSignupConverter();

	private AuthUserForSignupConverter() {
		super(AuthUser.class, AuthUserForSignupDTO.class, AuthUserForSignupDTO[]::new);
	}

	public static @NonNull AuthUserForSignupConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull AuthUserForSignupDTO convert(final @NonNull AuthUser authUser) {
		final AuthUserForSignupDTO authUserForSignupDTO = new AuthUserForSignupDTO(
			authUser.getEmail(),
			null,
			null,
			authUser.getName(),
			authUser.getSurnames(),
			authUser.getLanguage().getCode()
		);
		return authUserForSignupDTO;
	}
}