package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class AuthUserSignUpConfirmationFactory extends AbstractEntityFactory<AuthUserSignUpConfirmation> {
	public AuthUserSignUpConfirmationFactory() {
		super();
	}

	@Override
	protected @NonNull AuthUserSignUpConfirmation createInstance() {
		return new AuthUserSignUpConfirmation();
	}
}
