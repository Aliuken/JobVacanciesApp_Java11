package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserCurriculumDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;

public class AuthUserCurriculumConverter extends EntityToDtoConverter<AuthUserCurriculum, AuthUserCurriculumDTO> {

	private static final AuthUserCurriculumConverter SINGLETON_INSTANCE = new AuthUserCurriculumConverter();

	private AuthUserCurriculumConverter() {
		super(AuthUserCurriculumConverter::conversionFunction, AuthUserCurriculum.class, AuthUserCurriculumDTO.class, AuthUserCurriculumDTO[]::new);
	}

	public static AuthUserCurriculumConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static AuthUserCurriculumDTO conversionFunction(final AuthUserCurriculum authUserCurriculum) {
		final AuthUserCurriculumDTO authUserCurriculumDTO;
		if((authUserCurriculum != null) ) {
			final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(authUserCurriculum.getAuthUser());

			authUserCurriculumDTO= new AuthUserCurriculumDTO(
				authUserCurriculum.getId(),
				authUserDTO,
				null,
				authUserCurriculum.getFileName(),
				authUserCurriculum.getDescription()
			);
		} else {
			authUserCurriculumDTO = AuthUserCurriculumDTO.getNewInstance();
		}
		return authUserCurriculumDTO;
	}
}