package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class AuthUserService extends AbstractEntityServiceSuperclass<AuthUser> {

	public abstract AuthUser findByEmail(final String email);

	public abstract void lock(final Long authUserId);

	public abstract void unlock(final Long authUserId);

}
