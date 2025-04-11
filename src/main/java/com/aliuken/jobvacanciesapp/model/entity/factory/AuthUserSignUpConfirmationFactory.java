package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserSignUpConfirmationFactory extends AbstractEntityFactory<AuthUserSignUpConfirmation> {
	public AuthUserSignUpConfirmationFactory() {
		super();
	}

	@Override
	protected AuthUserSignUpConfirmation createInstance() {
		return new AuthUserSignUpConfirmation();
	}
}
