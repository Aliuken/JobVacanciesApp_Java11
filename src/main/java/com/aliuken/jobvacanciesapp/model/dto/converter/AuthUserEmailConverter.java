package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserEmailDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import org.jspecify.annotations.NonNull;

public class AuthUserEmailConverter extends EntityToDtoConverter<AuthUserCredentials, AuthUserEmailDTO> {

	private static final @NonNull AuthUserEmailConverter SINGLETON_INSTANCE = new AuthUserEmailConverter();

	private AuthUserEmailConverter() {
		super(AuthUserCredentials.class, AuthUserEmailDTO.class, AuthUserEmailDTO[]::new);
	}

	public static @NonNull AuthUserEmailConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull AuthUserEmailDTO convert(final @NonNull AuthUserCredentials authUserCredentials) {
		final AuthUserEmailDTO authUserEmailDTO = new AuthUserEmailDTO(
			authUserCredentials.getId(),
			authUserCredentials.getEmail()
		);
		return authUserEmailDTO;
	}
}