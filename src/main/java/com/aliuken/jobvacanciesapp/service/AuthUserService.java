package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityExtraServiceInterface;

public interface AuthUserService extends AbstractEntityExtraServiceInterface<AuthUser> {

	AuthUser findByEmail(String email);

	int lock(Long authUserId);

	int unlock(Long authUserId);

}
