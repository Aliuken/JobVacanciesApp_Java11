package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class AuthUserConfirmationService extends AbstractEntityServiceSuperclass<AuthUserConfirmation> {

	public abstract AuthUserConfirmation findByEmail(final String email);

	public abstract AuthUserConfirmation findByEmailAndUuid(final String email, final String uuid);

}