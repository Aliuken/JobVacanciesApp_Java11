package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class AuthUserFactory extends AbstractEntityFactory<AuthUser> {
	public AuthUserFactory() {
		super();
	}

	@Override
	protected @NonNull AuthUser createInstance() {
		return new AuthUser();
	}
}
