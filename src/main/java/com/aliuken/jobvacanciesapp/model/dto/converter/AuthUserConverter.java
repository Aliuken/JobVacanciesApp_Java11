package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;

public class AuthUserConverter extends EntityToDtoConverter<AuthUser, AuthUserDTO> {

	private static final AuthUserConverter SINGLETON_INSTANCE = new AuthUserConverter();

	private AuthUserConverter() {
		super(AuthUserConverter::conversionFunction, AuthUser.class, AuthUserDTO.class, AuthUserDTO[]::new);
	}

	public static AuthUserConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static AuthUserDTO conversionFunction(final AuthUser authUser) {
		final AuthUserDTO authUserDTO;
		if(authUser != null) {
			authUserDTO= new AuthUserDTO(
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
		} else {
			authUserDTO = AuthUserDTO.getNewInstance();
		}
		return authUserDTO;
	}
}