package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class AuthUserSignUpConfirmationService extends AbstractEntityServiceSuperclass<AuthUserSignUpConfirmation> {

	public abstract AuthUserSignUpConfirmation findByEmail(final String email);

	public abstract AuthUserSignUpConfirmation findByEmailAndUuid(final String email, final String uuid);

}