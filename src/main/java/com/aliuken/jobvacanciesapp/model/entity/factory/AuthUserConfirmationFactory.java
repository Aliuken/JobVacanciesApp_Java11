package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserConfirmationFactory extends AbstractEntityFactory<AuthUserConfirmation> {
	public AuthUserConfirmationFactory() {
		super();
	}

	@Override
	protected AuthUserConfirmation createInstance() {
		return new AuthUserConfirmation();
	}
}
