package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserFactory extends AbstractEntityFactory<AuthUser> {
	public AuthUserFactory() {
		super();
	}

	@Override
	protected AuthUser createInstance() {
		return new AuthUser();
	}
}
