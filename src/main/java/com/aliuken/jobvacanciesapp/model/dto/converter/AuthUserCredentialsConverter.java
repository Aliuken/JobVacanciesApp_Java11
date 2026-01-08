package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserCredentialsDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import org.jspecify.annotations.NonNull;

public class AuthUserCredentialsConverter extends EntityToDtoConverter<AuthUserCredentials, AuthUserCredentialsDTO> {

	private static final @NonNull AuthUserCredentialsConverter SINGLETON_INSTANCE = new AuthUserCredentialsConverter();

	private AuthUserCredentialsConverter() {
		super(AuthUserCredentials.class, AuthUserCredentialsDTO.class, AuthUserCredentialsDTO[]::new);
	}

	public static @NonNull AuthUserCredentialsConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull AuthUserCredentialsDTO convert(final @NonNull AuthUserCredentials authUserCredentials) {
		final AuthUserCredentialsDTO authUserCredentialsDTO = new AuthUserCredentialsDTO(
			authUserCredentials.getId(),
			authUserCredentials.getEmail(),
			null,
			null,
			null
		);
		return authUserCredentialsDTO;
	}
}