package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class AuthUserCredentialsFactory extends AbstractEntityFactory<AuthUserCredentials> {
	public AuthUserCredentialsFactory() {
		super();
	}

	@Override
	protected @NonNull AuthUserCredentials createInstance() {
		return new AuthUserCredentials();
	}
}