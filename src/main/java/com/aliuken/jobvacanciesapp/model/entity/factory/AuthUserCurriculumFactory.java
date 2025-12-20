package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserCurriculumFactory extends AbstractEntityFactory<AuthUserCurriculum> {
	public AuthUserCurriculumFactory() {
		super();
	}

	@Override
	protected AuthUserCurriculum createInstance() {
		return new AuthUserCurriculum();
	}
}
