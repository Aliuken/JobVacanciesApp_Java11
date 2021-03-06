package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityWithAuthUserExtraServiceInterface;

public interface AuthUserCurriculumService extends AbstractEntityWithAuthUserExtraServiceInterface<AuthUserCurriculum> {
	AuthUserCurriculum findByAuthUserAndFileName(AuthUser authUser, String fileName);
}
