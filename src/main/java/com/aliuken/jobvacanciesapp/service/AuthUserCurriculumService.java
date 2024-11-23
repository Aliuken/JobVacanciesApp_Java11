package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityWithAuthUserServiceSuperclass;

public abstract class AuthUserCurriculumService extends AbstractEntityWithAuthUserServiceSuperclass<AuthUserCurriculum> {

	public abstract AuthUserCurriculum findByAuthUserAndFileName(final AuthUser authUser, final String fileName);

}
