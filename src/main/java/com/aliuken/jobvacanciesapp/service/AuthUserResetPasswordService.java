package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class AuthUserResetPasswordService extends AbstractEntityServiceSuperclass<AuthUserResetPassword> {

	public abstract AuthUserResetPassword findByEmail(final String email);

	public abstract AuthUserResetPassword findByEmailAndUuid(final String email, final String uuid);

}