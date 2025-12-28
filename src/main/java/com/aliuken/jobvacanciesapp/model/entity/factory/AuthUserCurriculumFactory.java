package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class AuthUserCurriculumFactory extends AbstractEntityFactory<AuthUserCurriculum> {
	public AuthUserCurriculumFactory() {
		super();
	}

	@Override
	protected @NonNull AuthUserCurriculum createInstance() {
		return new AuthUserCurriculum();
	}
}
