package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import org.jspecify.annotations.NonNull;

public class AuthUserConverter extends EntityToDtoConverter<AuthUser, AuthUserDTO> {

	private static final @NonNull AuthUserConverter SINGLETON_INSTANCE = new AuthUserConverter();

	private AuthUserConverter() {
		super(AuthUser.class, AuthUserDTO.class, AuthUserDTO[]::new);
	}

	public static @NonNull AuthUserConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull AuthUserDTO convert(final @NonNull AuthUser authUser) {
		final AuthUserDTO authUserDTO= new AuthUserDTO(
			authUser.getId(),
			authUser.getEmail(),
			authUser.getName(),
			authUser.getSurnames(),
			authUser.getLanguage().getCode(),
			authUser.getEnabled(),
			authUser.getColorMode().getCode(),
			authUser.getInitialCurrency().getSymbol(),
			authUser.getInitialTableSortingDirection().getCode(),
			authUser.getInitialTablePageSize().getValue(),
			authUser.getPdfDocumentPageFormat().getCode(),
			authUser.getMaxPriorityAuthRoleName(),
			authUser.getAuthRoleNames()
		);
		return authUserDTO;
	}
}