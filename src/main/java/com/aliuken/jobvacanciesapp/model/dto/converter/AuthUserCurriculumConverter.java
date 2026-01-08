package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserCurriculumDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import org.jspecify.annotations.NonNull;

public class AuthUserCurriculumConverter extends EntityToDtoConverter<AuthUserCurriculum, AuthUserCurriculumDTO> {

	private static final @NonNull AuthUserCurriculumConverter SINGLETON_INSTANCE = new AuthUserCurriculumConverter();

	private AuthUserCurriculumConverter() {
		super(AuthUserCurriculum.class, AuthUserCurriculumDTO.class, AuthUserCurriculumDTO[]::new);
	}

	public static @NonNull AuthUserCurriculumConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull AuthUserCurriculumDTO convert(final @NonNull AuthUserCurriculum authUserCurriculum) {
		final AuthUser authUser = authUserCurriculum.getAuthUser();
		final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(authUser);

		final AuthUserCurriculumDTO authUserCurriculumDTO = new AuthUserCurriculumDTO(
			authUserCurriculum.getId(),
			authUserDTO,
			null,
			authUserCurriculum.getFileName(),
			authUserCurriculum.getDescription()
		);
		return authUserCurriculumDTO;
	}
}