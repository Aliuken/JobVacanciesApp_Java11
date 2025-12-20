package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserCredentialsFactory extends AbstractEntityFactory<AuthUserCredentials> {
	public AuthUserCredentialsFactory() {
		super();
	}

	@Override
	protected AuthUserCredentials createInstance() {
		return new AuthUserCredentials();
	}
}